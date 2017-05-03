/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author yao
 */
public class FileCompression {
    
    public void compressFile(String name) {
        try {
            FileInputStream fileInputStream = null;
            
            
            File file = new File("/home/yao/NetBeansProjects/OESimple/student screen shot");
                        ZipOutputStream zipOutputStream = 
                    new ZipOutputStream(new BufferedOutputStream(new FileOutputStream("/home/yao/NetBeansProjects/OESimple/" + name +".zip")) );
            
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            zipOutputStream.setLevel(1);
            
            
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for (File file1 : files) {
                    fileInputStream = new FileInputStream(file1);
                    zipOutputStream.putNextEntry(new ZipEntry(file1.getName()));
                }
            }
            
            int byteNo;
            byte[] b = new byte[64];
            while( (byteNo = fileInputStream.read(b)) > 0){
                zipOutputStream.write(b,0,byteNo);//將檔案寫入壓縮檔
            }
            zipOutputStream.close();
            fileInputStream.close();
            
            
        } catch (FileNotFoundException fnfe) {
            Logger.getLogger(FileCompression.class.getName()).log(Level.SEVERE, null, fnfe);
        } catch (IOException ioe) {
            Logger.getLogger(FileCompression.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }
}
