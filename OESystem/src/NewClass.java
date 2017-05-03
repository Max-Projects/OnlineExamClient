
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yao
 */
public class NewClass {
     public static void main(String[] args) throws IOException {
        File file = new File(System.getProperty("user.home") + File.separator + "temp" + File.separator + "132.png");
        if(!file.exists()) {
            file.mkdirs();
            
        }
     }
//     public byte[] convet2byte(BufferedImage i) {
//        try{
//            ByteArrayOutputStream baos = null;
//   
//            baos = new ByteArrayOutputStream();
//            ImageIO.write(i, "png", baos);
//            System.out.println(baos.size());
//            baos.flush();
//            imageInByte = encoder.encode(baos.toByteArray());    
//        } catch (IOException ioe){
//            ioe.printStackTrace();
//        }
//        return imageInByte;
//    }
}
