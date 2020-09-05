/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltudjava_chatapplication;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author WIN-10
 */
public class TransferFile {
    
  
    public boolean sendFile(ObjectOutputStream oos, File file)
    {
      
       
                FileInfo fileInfo = new FileInfo();
                fileInfo.setSourceDir(file.getPath());          
                fileInfo.setFileName(file.getName());
                long filesize = (int)file.length();
                fileInfo.setFileSize(filesize);
                DataInputStream dis = null;
            try {
                
                dis = new DataInputStream(new FileInputStream(file));
                byte[] fileData = new byte[(int)filesize];
                
                int read =0;
                int count =0;
                while((read< fileData.length) && (count = dis.read(fileData,read,fileData.length-read))>=0)
                {
                    read+=count;
                }
                fileInfo.setFileData(fileData);
                fileInfo.setStatus(1);             
                oos.writeObject(fileInfo);
                oos.flush();
                System.out.println("Da gui file");
                return true;
                
            } catch (FileNotFoundException ex) {
                
                Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
                fileInfo.setStatus(0);
                return false;
            } catch (IOException ex) {
                System.out.println("Lỗi writeObject và disread Transfer - send");
                Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
                fileInfo.setStatus(0);
                return false;
            } finally {
                try {
                    dis.close();
                } catch (IOException ex) {
                    System.out.println("Lỗi đóng dis Transfer - send");
                    Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
       
    }
    
    
    public FileInfo receiveFile(ObjectInputStream ois, String destinationDir)
    {
        try {
            
            FileInfo fileInfo = (FileInfo)ois.readObject();
            
            if(fileInfo.getStatus()==1){
                
              
                String filename = fileInfo.getFileName();
                String outputFile = destinationDir+filename;
                
                if(!new File(destinationDir).exists())
                {
                    new File(destinationDir).mkdirs();
                }
                File outFile = new File(outputFile);
                FileOutputStream fos = new FileOutputStream(outFile);
                fos.write(fileInfo.getFileData());
                fos.flush();
                fos.close();
                
                System.out.println("Da save file tai "+outputFile);
                return fileInfo;
            }
            
        } catch (IOException ex) {
            System.out.println("Lỗi ghi file trong Transfer - receive");
            Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            System.out.println("Lỗi đọc file trong Transfer - receive");
            Logger.getLogger(TransferFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }
    
}
