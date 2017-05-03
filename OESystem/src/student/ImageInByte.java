/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Yao
 */
public class ImageInByte {
    private byte[] imageInByte;

    
    public byte[] convet2byte(String file_path) {
        
        try{
            // open image
            File file = new File("student screen shot");

            File[] flist = file.listFiles();
            System.out.println("file length: " + flist.length);

            ByteArrayOutputStream baos = null;

            for (int i=1; i<flist.length;i++){
                System.out.println("file: " + flist[i]);
                BufferedImage image = ImageIO.read(flist[i]);
                baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", baos);
                baos.flush();
            }
            imageInByte = baos.toByteArray();
        } catch (Exception e){
            e.printStackTrace();
        }
        return imageInByte;
    }
}
