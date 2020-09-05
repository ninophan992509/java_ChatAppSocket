/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltudjava_chatapplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ltudjava_chatapplication.Group;
import org.hibernate.annotations.Parent;

/**
 *
 * @author WIN-10
 */
public class Server{

    Vector<UserThread> ThreadManager;
    Vector<String> Users;
    Vector<Group> Groups;
    ServerSocket serverSocket;
    int port=1999;
   
    
    public Server()
    {      
        createServer();
    }
    
    public static void main(String[] args) {
        Server server = new Server();
        server.createServer();
    }
   

    public void createServer() {
        ThreadManager = new Vector<UserThread>();
        Users = new Vector<String>();
        Groups = new Vector<Group>();
        serverSocket = null;

        try {

            serverSocket = new ServerSocket(port);      
            System.out.println("Server is running at port " + port);
            while (true) {
                try {

                    Socket NewClient = serverSocket.accept();
                    System.out.println("Co nguoi ket noi");

                    OutputStream output = NewClient.getOutputStream();
                    InputStream input = NewClient.getInputStream();

                    ObjectOutputStream oos = new ObjectOutputStream(output);
                    ObjectInputStream ois = new ObjectInputStream(input);

                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output,"UTF-8"), true);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"));

                    ThreadManager.add(new UserThread(NewClient, reader, writer, ois, oos));
                } catch (Exception e) {
                    System.out.println("Lỗi kết nối từ một client");
                }
            }

        } catch (IOException e) {
            System.out.println("Lỗi khởi tạo server");
           
        }finally{
             CloseServer();
        }

    }
    
    public void CloseServer()
    {
         if (serverSocket != null && !serverSocket.isClosed()) {

                try {
                   
                    serverSocket.close();
                    System.out.println("Đã đóng server");
                } catch (IOException ex) {
                    System.out.println("Lỗi đóng server");
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
             System.exit(0);
            }
    }
  

    public void Send(String sender, String receiver, String msg) {
        for (UserThread c : ThreadManager) {
            if (c.getUsername().equals(receiver)) {
                c.sendMessage(sender, msg);
                break;
            }
        }
    }

    public void DeleteMember(String nameGroup, String nameMember) {
        for (Group gr : Groups) {
            if (gr.getNameGroup().equals(nameGroup)) {

                for (String name : gr.getGroupList()) {
                    if (name.equals(nameMember)) {
                        gr.getGroupList().remove(name);
                        break;
                    }
                }
                break;
            }
        }
    }

    public void SendMessToGroup(String nameGroup, String sender, String msg) {

        for (Group gr : Groups) {
            if (gr.getNameGroup().equals(nameGroup)) {
                for (UserThread c : ThreadManager) {
                    if (gr.getGroupList().contains(c.getUsername()) && !c.getUsername().equals(sender)) {
                        System.out.println("sending mess... Group:" + nameGroup + ";sender: " + sender + "; receiver:" + c.getUsername());
                        c.sendMessToMemeber(nameGroup, sender, msg);

                    }
                }
                break;
            }
        }
    }

    public void SendFileToGroup(String nameGroup, String sender, FileInfo fileInfo,String destinationDirTemp) {

        for (Group gr : Groups) {
            if (gr.getNameGroup().equals(nameGroup)) {
                for (UserThread c : ThreadManager) {
                    if (gr.getGroupList().contains(c.getUsername()) && !c.getUsername().equals(sender)) {
                        System.out.println("sending file... Group:" + nameGroup + ";sender: " + sender + "; receiver:" + c.getUsername());
                        c.sendFileToMember(nameGroup, sender, fileInfo,destinationDirTemp);
                    }
                }
                break;
            }
        }
    }

    public boolean SendFileToReceiver(String sender, String receiver, FileInfo fileInfo, String destinationDirTemp) {
        for (UserThread c : ThreadManager) {
            if (c.getUsername().equals(receiver)) {
                return c.sendFile(sender, fileInfo,destinationDirTemp);

            }
        }
        return false;
    }

    public void SendListToAll() {
        for (UserThread c : ThreadManager) {
            c.sendList();
        }
    }

    public void SendGroupToMembers(Group group) {
        for (UserThread c : ThreadManager) {
            if (group.getGroupList().contains(c.getUsername())) {

                System.out.println("sending...group " + c.getUsername());
                c.sendGroup(group);
            }
        }
    }

    class UserThread extends Thread {

        String username = "";
        BufferedReader reader;
        PrintWriter writer;
        Socket socket;
        ObjectInputStream ois;
        ObjectOutputStream oos;

        private volatile boolean running = true;

        public UserThread(Socket client, BufferedReader reader, PrintWriter writer, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
            this.socket = client;
            this.reader = reader;
            this.writer = writer;
            this.oos = objectOutputStream;
            this.ois = objectInputStream;
            start();

        }

        public String getUsername() {
            return username;
        }

        public void sendMessage(String sender, String msg) {
            writer.println(sender);
            writer.println(msg);
            writer.println("***");
        }

        public void sendList() {
            writer.println("server"); //mở đầu tin nhắn
            for (String user : Users) {
                writer.println(user);
            }
            writer.println("***");//kết thúc tin nhắn

        }

        public boolean sendFile(String sender, FileInfo fileInfo, String destinationDirTemp) {
            writer.println("file");
            writer.println(sender);
            TransferFile tf = new TransferFile();
            return tf.sendFile(oos, new File(destinationDirTemp + fileInfo.getFileName()));
        }

        public boolean sendFileToMember(String nameGroup, String sender, FileInfo fileInfo,String destinationDirTemp) {
            writer.println("group");
            writer.println("file-group");
            writer.println(nameGroup);
            writer.println(sender);
            TransferFile tf = new TransferFile();
            return tf.sendFile(oos, new File(destinationDirTemp+ fileInfo.getFileName()));
        }

        public void sendMessToMemeber(String nameGroup, String sender, String msg) {
            writer.println("group");
            writer.println(nameGroup);
            sendMessage(sender, msg);
        }

        public void sendGroup(Group newGroup) {

            writer.println("new-group");
            writer.println(newGroup.getNameGroup()); 
            newGroup.getGroupList().forEach((element)->{
                writer.println(element);
            });
            writer.println("***");
            

        }

        public void sendError(String error) {
            writer.println("error");
            writer.println(error);
            writer.println("***");

        }

        public void closeSocket() {

            running = false;
            ThreadManager.remove(this);
            Users.remove(getUsername());
            SendListToAll();

            try {

                writer.close();
                reader.close();
                ois.close();
                socket.close();
                oos.close();
                System.out.println("Da dong socket cua " + username);

            } catch (IOException ex) {

                System.out.println("Lỗi đóng socket");
            }

        }

        @Override
        public void run() {

            String line = "", receiver;

            try {
                username = reader.readLine();
                Users.add(username);
                SendListToAll();
                while (running && !socket.isClosed() && line != null) {

                    line = reader.readLine();
                    System.out.println(line);

                    if (line.equals("end")) {
                        System.out.println(getUsername() + " ngat ket noi");

                        closeSocket();
                    } else if (line.equals("file")) {
                        receiver = reader.readLine();
                        //Đọc File từ input vào file tạm thời 
                        TransferFile tf = new TransferFile();
                        String destinationDirTemp="C:/tmp/downloads/";
                        FileInfo fileInfo = tf.receiveFile(ois,destinationDirTemp);
                        if (fileInfo != null) {

                            if (SendFileToReceiver(getUsername(), receiver, fileInfo,destinationDirTemp)) {
                                sendMessage(receiver, "Da gui file!!!");
                            } else {
                                sendMessage(receiver, "Loi gui file!!!");
                            }

                            File file = new File(destinationDirTemp + fileInfo.getFileName());
                            if (file.exists()) {
                                file.delete();
                                System.out.println("Da xoa file tai server");
                            }

                        } else {
                            sendMessage(receiver, "Loi gui file!!!");
                        }
                    } else if (line.equals("new-group")) {
                        line = reader.readLine();
                        String nameGroup = line;
                        Group newGroup = new Group();
                        newGroup.setNameGroup(nameGroup);
                        line = reader.readLine();
                        while(line.compareTo("***")!=0)
                        {
                            newGroup.getGroupList().add(line);
                            line = reader.readLine();
                            
                        }
                        
                         boolean isExist = false;
                         for (Group g : Groups) {
                         if (g.getNameGroup().compareTo(newGroup.getNameGroup()) == 0) {
                              isExist = true;
                              break;
                           
                         }}
                         if(isExist)
                         {
                             writer.println("error");
                             writer.println("Không thể tạo nhóm. Hãy đổi tên khác");
                             writer.println("***");
                             writer.flush();
                         }else{
                             Groups.add(newGroup);
                             SendGroupToMembers(newGroup);
                         }          

                    } else if (line.equals("group")) {
                        line = reader.readLine();
                        System.out.println(line);
                        if (line.equals("file-group")) {
                            line = reader.readLine();
                            String nameGroup = line;
                            System.out.println("File từ group"+nameGroup);
                            TransferFile tf = new TransferFile();
                            String destinationDirTemp="C:/tmp/downloads/";
                            FileInfo fileInfo = tf.receiveFile(ois,destinationDirTemp);
                            if (fileInfo != null) {
                                SendFileToGroup(nameGroup, username, fileInfo,destinationDirTemp);
                                File file = new File(destinationDirTemp + fileInfo.getFileName());
                                if (file.exists()) {
                                    file.delete();
                                    System.out.println("Da xoa file tai server");
                                }

                            }
                        } else if (line.equals("error")) {
                            line = reader.readLine();
                            System.out.println("Error group từ "+line);
                            String nameGroup = line;
                            DeleteMember(nameGroup, username);
                            System.out.println("Đã xóa "+username+" ra khỏi "+nameGroup);
                        } else {
                            String groupName = line;
                            System.out.println("Nhận mess từ group "+line);
                            line = reader.readLine();
                            String msg = "";
                            while (line.compareTo("***") != 0) {
                                msg += (line + "\n");
                                line = reader.readLine();
                            }
                            SendMessToGroup(groupName, username, msg);
                            System.out.println("Vừa gửi mess của "+username+" cho group: "+groupName);
                        }
                    } else {
                        
                        receiver = line;
                        System.out.println(username+ "dang gui tin nhan cho "+receiver);
                        line = reader.readLine();
                        String msg = "";
                        while (line.compareTo("***") != 0) {
                            msg += (line + "\n");
                            line = reader.readLine();
                        }
                        Send(getUsername(), receiver, msg);

                    }
                }

            } catch (IOException e) {
                System.out.println("Lỗi readline trong thread");
            }
        }

    }
   
}
