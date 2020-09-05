/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltudjava_chatapplication;

import java.awt.Color;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



/**
 *
 * @author WIN-10
 */
public class ChatTab extends javax.swing.JPanel {

    /**
     * Creates new form ChatTab
     */
    public ChatTab() {
        initComponents();
    }
    
    String host, guest;
    Socket clientSocket;
    PrintWriter writer;
    ObjectOutputStream oos;
    JFrame EmojiFrame ;
    public ChatTab(String host, String guest, Socket socket, PrintWriter writer,ObjectOutputStream objectOutputStream) {
         
        initComponents();           
        this.host = host;
        this.guest =  guest;
        this.clientSocket = socket;
        this.writer = writer;
        this.oos = objectOutputStream;
       
        EmojiFrame = new JFrame("Emoji");
        EmojiFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        EmojiFrame.add(new ListPanel(this));
        EmojiFrame.pack();
        EmojiFrame.setLocationRelativeTo(null);
        EmojiFrame.setVisible(false);
        
      
    }
     
    public String getGuest()
    {
        return this.guest;
    }
    
    public void HienThiTinNhan(String name,String msg)
    {
        jTextArea1.setForeground(Color.pink);
        jTextArea1.setBackground(Color.black);
        jTextArea1.append(name+": "+msg);
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
        
       
    }
    
    public void AppendMessToBoxChat(String name, String msg)
    {
        jTextArea1.setForeground(Color.orange);
        jTextArea1.setBackground(Color.red);
        jTextArea1.append(name+": "+msg);
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());

    }
    
    public void AppendIcon(String icon)
    {
        jTextArea2.append(icon);
        jTextArea2.setCaretPosition(jTextArea2.getDocument().getLength());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jFileName = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 255, 51)));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 22)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(0, 255, 51));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jFileName.setEditable(false);
        jFileName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileNameActionPerformed(evt);
            }
        });
        jPanel1.add(jFileName, new org.netbeans.lib.awtextra.AbsoluteConstraints(39, 11, 263, 30));

        jButton2.setText("Browser");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 14, 24, 24));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon("C:\\Users\\WIN-10\\Desktop\\phone.png")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 24, 24));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon("C:\\Users\\WIN-10\\Desktop\\smile (1).png")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 24, 24));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon("C:\\Users\\WIN-10\\Desktop\\video-call (1).png")); // NOI18N
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, 24, 24));

        jButton7.setText("SEND FILE");
        jButton7.setActionCommand("");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(338, 15, -1, -1));

        jButton1.setText("SEND");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(459, 47, 76, 46));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("File");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 16, -1, -1));

        jTextArea2.setBackground(new java.awt.Color(255, 255, 0));
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        jTextArea2.setForeground(new java.awt.Color(255, 51, 0));
        jTextArea2.setRows(5);
        jScrollPane3.setViewportView(jTextArea2);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 400, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(!jTextArea2.getText().isEmpty())
        {
            
            AppendMessToBoxChat("Bạn", jTextArea2.getText()+"\n");
            if(guest.contains("Group:"))
            {
               writer.println("group");
               writer.println(guest.substring(6, guest.length()));               
            }else{
              writer.println(guest);
            }
            writer.println(jTextArea2.getText());
            writer.println("***");
        
            jTextArea2.setText(null);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jFileNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFileNameActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         JFileChooser jFileChooser = new JFileChooser();
         int result = jFileChooser.showOpenDialog(this);
         if(result== jFileChooser.APPROVE_OPTION)
        {
            File selectedFile = jFileChooser.getSelectedFile();     
            jFileName.setText(selectedFile.getPath());
            
        }
       
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        if(!jFileName.getText().isEmpty())
        {
            //writer.println(guest);
            File file = new File(jFileName.getText());
            if(file.exists()&& !file.isDirectory() )
            {               

                   
                if (guest.contains("Group:")) {
                    
                    writer.println("group");
                    writer.println("file-group");
                    writer.println(guest.substring(6, guest.length()));
                } else {
                    writer.println("file");
                    writer.println(guest);
                }

                    writer.flush();
                    TransferFile transferFile = new TransferFile();
                    if(transferFile.sendFile(oos, file)){
                        AppendMessToBoxChat("Bạn", " vua gui file "+file.getName()+" cho "+guest+"\n");
                        
                    }else{
                        AppendMessToBoxChat("Error", ": Loi gui file");
                    }
               
                     jFileName.setText(null);
            }else{
                JOptionPane.showMessageDialog(null, "File không tồn tại");
            }
            
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        EmojiFrame.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JTextField jFileName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}