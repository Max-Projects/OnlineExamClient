/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teacher;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javax.imageio.ImageIO;

/**
 *
 * @author yao
 */
public class SnapshotLoad {
//    private int numOfsnapshots;
    private final Base64.Decoder decoder = Base64.getDecoder();
    private BufferedImage read = null;
    private ImageView imageView; 
    private static GridPane gridPane = new GridPane();
    private Pane monitorPane = new Pane();
    private Label label;
    
    int mb = 1024*1024;
    Runtime runtime = Runtime.getRuntime();
    
    public SnapshotLoad(Pane monitorPane) {
        this.monitorPane = monitorPane;
    }

    public GridPane loadHistoryImage(List snapshots) {
        int numOfsnapshots = snapshots.size();
        return gridPane;
    }
//    public GridPane loadHistoryImage(List snapshots) {
//        int j = 0;
//        numOfsnapshots = snapshots.size();
//        int x = numOfsnapshots - 3;
//        for (int i = x; i < numOfsnapshots; i++) {
//            if(numOfsnapshots != 0){
//                byte[] b = decoder.decode(((Map)snapshots.get(i)).get("snapshot").toString().getBytes());
//                try {
//                    read = ImageIO.read(new ByteArrayInputStream(b));
//
//                    Image image = SwingFXUtils.toFXImage(read, null);
//                    imageView = createImageViewEvent(image);
//                    imageView.setFitHeight(120);
//                    imageView.setFitWidth(170);
//                    
//                    label = new Label(((Map)snapshots.get(i)).get("time").toString());
//                
////                if(i == 0 | i == 1) {
//                    GridPane.setConstraints(imageView, j%2, j/2);
//                    GridPane.setConstraints(label, (j+1)%2, (j+1)/2);
//                    j += 2;
////               } else {
////                    GridPane.setConstraints(imageView, (j+2)%2, (j+2)/2);
////                    GridPane.setConstraints(label, (j)%2, (j)/2);
////                    j += 4;
////                }
//
////                GridPane.setConstraints(imageView, i%2, i/2);
//                    
//                    gridPane.setHgap(5);
//                    gridPane.getChildren().addAll(imageView, label);
//
//                } catch (IOException ex) {
//                    Logger.getLogger(TeacherController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//        gridPane.setVgap(10);
//        
//        System.out.println("###### Memory ###### Used: " + (runtime.totalMemory()-runtime.freeMemory())/mb);
//        return gridPane;
//    }
    
    public GridPane getLatestImage(List snapshots){
        int count=0;
        int numOfsnapshots = snapshots.size();
        gridPane.getChildren().clear();
        int x = numOfsnapshots - 3;
        int j = 0;
        for (int i = x; i < numOfsnapshots; i++) {
            if(numOfsnapshots != 0){
                byte[] b = decoder.decode(((Map)snapshots.get(i)).get("snapshot").toString().getBytes());
                
                try {                   
                    read = ImageIO.read(new ByteArrayInputStream(b));
                    Image image = SwingFXUtils.toFXImage(read, null);
                    imageView = createImageViewEvent(image);
                    imageView.setFitHeight(120);
                    imageView.setFitWidth(170);

                    count += 1 ;
                    System.out.println("=====\nWidth: " + image.getWidth() +  
                            "\nHeight: " + image.getHeight() + "\nKB: " + (b.length/1000) + "\n張數: " + count+"\n=====");

                    label = new Label(((Map)snapshots.get(i)).get("time").toString());

                    GridPane.setConstraints(imageView, j%2, j/2);
                    GridPane.setConstraints(label, (j+1)%2, (j+1)/2);
                    j += 2;
                    gridPane.setHgap(5);
                    gridPane.getChildren().addAll(imageView, label);

                } catch (IOException ex) {
                    Logger.getLogger(TeacherController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        numOfsnapshots += 1;
        System.out.println("###### Memory ###### Used: " + (runtime.totalMemory()-runtime.freeMemory())/mb);
        return gridPane;
    }
    
    private ImageView createImageViewEvent(Image i) {
        ImageView image = null;
        image = new ImageView(i);
        ImageView zoomInImage = new ImageView(i);
        monitorPane.getChildren().add(zoomInImage);
        zoomInImage.setVisible(false);
        
        image.setOnMouseEntered((MouseEvent event) -> {
            zoomInImage.setVisible(true);            
        });
        
        image.setOnMouseExited((MouseEvent event) -> {
            zoomInImage.setVisible(false);            
        });

        return image;
    }
}
