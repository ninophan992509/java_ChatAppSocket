/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltudjava_chatapplication;

import entities.Taikhoan;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

/**
 *
 * @author WIN-10
 */
public class ChatForm extends javax.swing.JFrame {

    /**
     * Creates new form ChatForm
     */
    String username;
    BufferedReader reader;
    PrintWriter writer;
    Socket ClienSocket;
    Vector<String> Users;
    Vector<Group> Groups;
    Vector<ChatTab> ChatList;
    MessageThread messageThread;
    InputStream input;
    OutputStream output;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    private String destinationDir ="C:/tmp/file-clients/";
    private volatile boolean running = true;
    TaiKhoanDao tkd;
    int port =1999;

    public ChatForm() {
        initComponents();
    }

    public ChatForm(String username) {

        initComponents();
        Users = new Vector<String>();
        Groups = new Vector<Group>();
        ChatList = new Vector<ChatTab>();
        tkd = new TaiKhoanDao();
        this.username = username;
        destinationDir+=(username+"/");
        Border border = BorderFactory.createTitledBorder(username);
        jTabbedPane1.setBorder(border);

        try {
            ClienSocket = new Socket("localhost", port);
            System.out.println(username + " ket noi!");
            output = ClienSocket.getOutputStream();
            input = ClienSocket.getInputStream();
            oos = new ObjectOutputStream(output);
            ois = new ObjectInputStream(input);
            
            writer = new PrintWriter(new OutputStreamWriter(output,"UTF-8"), true);           
            reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));
            writer.println(username);   
            messageThread = new MessageThread();
            messageThread.start();

        } catch (IOException e) {
            System.out.println("Lỗi khởi tạo ChatForm");
            System.out.println(e);
        }

    }

    public ChatTab CreateChatTab(String sender) {
        ChatTab cp = new ChatTab(username, sender, ClienSocket, writer, oos);
        ChatList.add(cp);
        jTabbedPane1.add(sender, cp);
        cp.setVisible(true);
        return cp;
    }

    public void LoadGroupList() {
        if(!Groups.isEmpty()){
        DefaultListModel dlm = new DefaultListModel();
        for (Group group : Groups) {
            dlm.addElement(group.getNameGroup());
        }
        jListGroup.setModel(dlm);
        jScrollPane2.setViewportView(jListGroup);
        }
    }

    public void LoadOptionList() {
        Users.remove(username);
        Object[] objArray = Users.toArray();
        Users.add(username);
        String[] group_list = Arrays.copyOf(objArray, objArray.length, String[].class);
        jList1 = new JList(group_list);
        jList1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane3.setViewportView(jList1);
    }

    public void ShowMessage(String line, String sender, ChatTab cp) {

        try {

            line = reader.readLine();
            String msg = "";
            while (line.compareTo("***") != 0) {
                msg += (line + "\n");
                line = reader.readLine();

            }
            cp.HienThiTinNhan(sender, msg);
        } catch (IOException ex) {
            System.out.println("Lỗi readline trong showMessage");
        }

    }

    public void ReceiveiFile(String sender, ChatTab cp) {
        
        
        TransferFile tf = new TransferFile();
        FileInfo fileInfo = tf.receiveFile(ois,destinationDir);

        if (fileInfo != null) {
            String msg = sender + " vua gui file " + fileInfo.getFileName() + " cho ban\n" + "Da tai file xuong tai " + destinationDir + fileInfo.getFileName() + "\n";
            cp.HienThiTinNhan("Thong bao", msg);
        }
    }

    class MessageThread extends Thread {

        @Override
        public void run() {
            String line = "";
            try {
                while (running && !ClienSocket.isClosed() && line != null) {

                    line = reader.readLine();
                    System.out.println(line);
                    if (line != null) {
                        if (line.equals("server")) {
                            DefaultListModel dlm = new DefaultListModel();
                            Users.removeAllElements();
                            line = reader.readLine();
                            while (line.compareTo("***") != 0) {

                                Users.add(line);
                                dlm.addElement(line);
                                line = reader.readLine();
                            }

                            jListUser.setModel(dlm);
                            jScrollPane1.setViewportView(jListUser);
                            LoadOptionList();

                            if (!ChatList.isEmpty()) {
                                for (Iterator<ChatTab> iterator = ChatList.iterator(); iterator.hasNext();) {
                                    ChatTab ct = iterator.next();
                                    if (!Users.contains(ct.getGuest()) && !ct.getGuest().contains("Group:")) {
                                        ct.setVisible(false);
                                        iterator.remove();
                                        jTabbedPane1.remove(ct);

                                    }
                                }

                            }
                        } else if (line.equals("error")) {
                            String error = "";
                            line = reader.readLine();
                            while (line.compareTo("***") != 0) {
                                error += (line + "\n");
                                line = reader.readLine();
                            }
                            JOptionPane.showMessageDialog(rootPane, "Error: " + error);
                        } else if (line.equals("new-group")) {
                               Group newGroup = new Group();
                               line = reader.readLine();
                               System.out.println(line);
                               newGroup.setNameGroup(line);
                               line = reader.readLine();
                               
                               while(line.compareTo("***")!=0)
                               {
                                   newGroup.getGroupList().add(line);
                                    line = reader.readLine();
                               }
                               Groups.add(newGroup);
                               LoadGroupList();
                               CreateChatTab("Group:"+newGroup.getNameGroup());
          
                        } else if (line.equals("group")) {
                            line = reader.readLine();
                            System.out.println("Name group:"+line);
                            String nameGroup = line;
                            line = reader.readLine();
                             System.out.println("Sender:"+line);
                            String sender = line;
                            int isFile = 0;

                            if (nameGroup.equals("file-group")) {
                                nameGroup = sender;
                                line = reader.readLine();
                                System.out.println("Nhận file group từ:"+line);
                                sender = line;
                                isFile = 1;

                            }

                            for (Iterator<ChatTab> iterator = ChatList.iterator(); iterator.hasNext();) {
                                ChatTab ct = iterator.next();
                                if (ct.getGuest().compareTo("Group:" + nameGroup) == 0) {

                                    if (isFile == 0) {
                                        System.out.println("Show mess...");
                                        ShowMessage(line, sender, ct);
                                    } else {
                                        System.out.println("Receiver File...");
                                        ReceiveiFile(sender, ct);
                                    }
                                    break;
                                }

                            }

                        } else {
                            String sender = line;                            
                            int isFile = 0;
                            if (sender.equals("file")) {
                                line = reader.readLine();
                                sender = line;
                                isFile = 1;//là file
                            }

                            if (ChatList.isEmpty()) {

                                ChatTab cp = CreateChatTab(sender);
                                if (isFile == 0) {
                                    ShowMessage(line, sender, cp);
                                } else {
                                    ReceiveiFile(sender, cp);
                                }

                            } else {
                                boolean isFound = false;
                                for (Iterator<ChatTab> iterator = ChatList.iterator(); iterator.hasNext();) {
                                    ChatTab ct = iterator.next();
                                    if (ct.getGuest().compareTo(sender) == 0) {

                                        isFound = true;
                                        if (isFile == 0) {
                                            ShowMessage(line, sender, ct);
                                        } else {
                                            ReceiveiFile(sender, ct);
                                        }
                                        break;
                                    }

                                }

                                if (!isFound) {

                                    ChatTab cp = CreateChatTab(sender);
                                    if (isFile == 0) {
                                        ShowMessage(line, sender, cp);
                                    } else {
                                        ReceiveiFile(sender, cp);
                                    }

                                }

                            }

                        }
                    }

                }
            } catch (IOException ex) {
                System.out.println("Lỗi readline trong thread");
                Logger.getLogger(ChatForm.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListUser = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListGroup = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Chat Window");
        setBackground(new java.awt.Color(0, 0, 0));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "HOST", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jListUser.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Friends", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 0, 0))); // NOI18N
        jListUser.setForeground(new java.awt.Color(102, 255, 102));
        jListUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jListUserMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jListUser);

        jButton1.setText("Đăng xuất");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setToolTipText("Nhập tên nhóm");

        jButton2.setText("Tạo nhóm");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jListGroup.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "My Groups", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 0, 0))); // NOI18N
        jListGroup.setForeground(new java.awt.Color(255, 102, 0));
        jListGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jListGroupMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jListGroup);

        jList1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Add To Group", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(204, 0, 255))); // NOI18N
        jScrollPane3.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jTextField1))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(272, 272, 272)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1))
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing


    }//GEN-LAST:event_formWindowClosing

    private void jListUserMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListUserMouseReleased
        // TODO add your handling code here:

        if (jListUser.getSelectedValue() != null) {
            String sender = jListUser.getSelectedValue();
            if (sender.compareTo(username) != 0) {
                if (ChatList.isEmpty()) {
                    CreateChatTab(sender);

                } else {
                    boolean isFound = false;
                    for (ChatTab cp : ChatList) {
                        if (cp.getGuest().compareTo(sender) == 0) {
                            isFound = true;
                            break;
                        }
                    }

                    if (!isFound) {
                        CreateChatTab(sender);
                    }

                }
            }
        }
    }//GEN-LAST:event_jListUserMouseReleased

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        Logout();
    }//GEN-LAST:event_formWindowClosed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        this.dispose();
        Logout();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String group_name = jTextField1.getText();
        if (!group_name.isEmpty()) {

            boolean isExist = false;
            for (Group g : Groups) {
                if (g.getNameGroup().compareTo(group_name.trim()) == 0) {
                    isExist = true;
                }
            }

            if (isExist) {
                JOptionPane.showMessageDialog(null, "Nhóm đã tồn tại");
            } else {

                List<String> groupList = jList1.getSelectedValuesList();

                if (groupList == null) {
                    JOptionPane.showMessageDialog(null, "Không thể tạo nhóm");
                } else {
                    if (!groupList.isEmpty()) {
                        
                            writer.println("new-group");
                            writer.println(group_name.trim());
                            writer.println(username);
                            groupList.forEach((element) -> {
                                writer.println(element);
                            });
                            writer.println("***");
                            System.out.println("Vua gui nhom moi");

                    } else {
                        JOptionPane.showMessageDialog(null, "Hãy chọn ít nhất 1 thành viên cho nhóm");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Bạn chưa nhập tên nhóm");
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jListGroupMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListGroupMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jListGroupMouseReleased

    private void CloseReader() {
        try {
            reader.close();
            ois.close();
        } catch (IOException ex) {
            System.out.println("Lỗi CloseReader");
            Logger.getLogger(ChatForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Closewriter() {
        try {
            writer.close();
            oos.close();
        } catch (IOException ex) {
            System.out.println("Lỗi CloseWriter");
            Logger.getLogger(ChatForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void CloseSocket() {
        try {
            ClienSocket.close();
        } catch (IOException ex) {
            System.out.println("Lỗi CloseSocket");
            Logger.getLogger(ChatForm.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private void Logout() {

        running = false;
        writer.println("end");
        writer.flush();
        CloseReader();
        Closewriter();
        CloseSocket();
        Taikhoan tk = tkd.find(username);
        if (tk != null) {
            tk.setTructuyen(false);
            tkd.update(tk);

        } else {
            System.out.println("Lỗi logout trên database");
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jListGroup;
    private javax.swing.JList<String> jListUser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
