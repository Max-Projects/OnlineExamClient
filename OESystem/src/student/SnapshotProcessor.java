package student;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.mislab.api.Student;

/**
 *
 * @author Yao
 */
public class SnapshotProcessor implements Runnable {
    private String file_path, studentId;
    private boolean flag_stop = true;
    private byte[] imageInByte;
    private Student student;
    private int courseId;
    private int examId;
    private final Base64.Encoder encoder = Base64.getEncoder();
    
    public SnapshotProcessor(){
        
    }
    
    public SnapshotProcessor(Student s, int c_Id, int e_Id, String s_Id){
        student = s;
        courseId = c_Id;
        examId = e_Id;
        studentId = s_Id;
    }
 
    @Override
    public void run(){
        System.out.println("**** Screen Shot Begin ****");

        while (flag_stop) {            
            try{
                // 更改截圖時間
                Thread.sleep(100);
                file_path = getDateTime();     
                sendSnapshot(snapshotScreen(file_path), file_path);   
            } catch (InterruptedException ie) {
                System.out.println("\n*** InterruptedException!!! ( ImageProcessor.run ) ***");
            } catch (NullPointerException npe) {
                System.out.println("\n*** NullPointerException!!! ( ImageProcessor.run ) ***");
            } catch (Exception e) {
                    Logger.getLogger(SnapshotProcessor.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        System.out.println("Screen Shot Stop");
    }
    
    public BufferedImage snapshotScreen(String fileName) throws Exception {
        Robot robot = new Robot();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect = new Rectangle(0, 0, d.width, d.height);
        BufferedImage image = robot.createScreenCapture(rect);
        file_path = fileName + "pic";
        File file = new File( System.getProperty("user.home") + File.separator + "temp" + File.separator);
        if(!file.exists()) {
            file.mkdirs();
        }
        ImageIO.write(image, "png", new File( System.getProperty("user.home") + File.separator + "temp" + File.separator + file_path + ".png"));
        
        return image;
    }
   
    private void sendSnapshot(BufferedImage i, String path) throws IOException {
        BufferedImage image = thumbnail(i);
        byte[] b = convet2byte(image);
        FileWriter fw = new FileWriter("record.txt", true);
        fw.write("FileName:" + path + "\nTime: " + getTime() + "\nWidth: " +
                image.getWidth() + "\nHeight: " + image.getHeight() + "\n===== ");
        fw.close();
        
        student.sendSnapshot(courseId, examId, studentId, b);
    }
    
    private String getTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss:SSSS");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }
        
    public BufferedImage thumbnail(BufferedImage i) throws IOException {
        
        //try {
          i = Thumbnails.of(i).scale(0.8f).asBufferedImage();
       // } catch (IOException ioe) {
       //     Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ioe);
        //}
        
        return i;
    }

    public byte[] convet2byte(BufferedImage i) {
        try{
            FileWriter fw = new FileWriter("record.txt", true);
            
            ByteArrayOutputStream baos = null;
   
            baos = new ByteArrayOutputStream();
            ImageIO.write(i, "png", baos);
            fw.write("=====\nStudent ID:" + studentId + "\nKB: " + baos.size()/1024+"\n");
            fw.close();
            baos.flush();
            
            imageInByte = encoder.encode(baos.toByteArray());    
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        return imageInByte;
    }
    
    
    public String getDateTime(){
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy_MM_dd hh_mm_ss");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }
    
    public String getFileName() {
        return file_path;
    }
    
    public void stop() {
        flag_stop = false;
    }
}
