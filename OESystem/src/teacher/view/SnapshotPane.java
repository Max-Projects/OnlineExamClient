/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teacher.view;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import teacher.ImageUtils;

/**
 *
 * @author Scott
 */
public class SnapshotPane extends ScrollPane {
    private static final Logger LOGGER = Logger.getLogger(SnapshotPane.class.getSimpleName());
    
    private static final int SNAPSHOT_HEIGHT = 120;
    private static final int SNAPSHOT_WIDTH = 170;
    
//    private final int numSnapshots;
    private int num = 1;
    private int count;
    
    private final GridPane contentPane;
    private final ImageView[] imageViews;
    private final Label[] labels;
    private final Label[] numLabels;
    
    public SnapshotPane(List st) {
        count = st.size();
        contentPane = new GridPane();
        contentPane.setHgap(5.0f);
        contentPane.setVgap(5.0f);
        contentPane.setPadding(new Insets(10,10,10,10));
        
//        numSnapshots = 3;
//        imageViews = new ImageView[numSnapshots];
//        labels = new Label[numSnapshots];
//        numLabels = new Label[numSnapshots];
//
//        for (int i = 0; i < numSnapshots; i++) {
//            imageViews[i] = new ImageView();
//            imageViews[i].setFitHeight(SNAPSHOT_HEIGHT);
//            imageViews[i].setFitWidth(SNAPSHOT_WIDTH);
//            
//            labels[i] = new Label();
//            numLabels[i] = new Label();
//            
//            contentPane.add(numLabels[i], 0, i);
//            contentPane.add(imageViews[i], 1, i);
//            contentPane.add(labels[i], 2, i);
//        }

        imageViews = new ImageView[count];
        labels = new Label[count];
        numLabels = new Label[count];
        
        for (int i = 0; i < count; i++) {
            imageViews[i] = new ImageView();
            imageViews[i].setFitHeight(SNAPSHOT_HEIGHT);
            imageViews[i].setFitWidth(SNAPSHOT_WIDTH);
            
            labels[i] = new Label();
            numLabels[i] = new Label(((Map)st.get(i)).get("name").toString());
            
            contentPane.add(numLabels[i], 0, i);
            contentPane.add(imageViews[i], 1, i);
            contentPane.add(labels[i], 2, i);
        }
        
        setContent(contentPane);
    }
    
    public void addSnapshot(String name, String base64Image, String createdAt) throws IOException {
//        for (int i = numSnapshots - 1; i > 0; i--) {
//            imageViews[i].setImage(imageViews[i - 1].getImage());
//            labels[i].setText(labels[i - 1].getText());
//            numLabels[i].setText(numLabels[i - 1].getText());
//        }
//        Image image = ImageUtils.decode(base64Image, SNAPSHOT_WIDTH, SNAPSHOT_HEIGHT);
//        imageViews[0].setImage(image);
//        labels[0].setText(createdAt);
//        numLabels[0].setText(String.valueOf(num++));
//        System.gc();
        Image image = ImageUtils.decode(base64Image, SNAPSHOT_WIDTH, SNAPSHOT_HEIGHT);
        write2file(image, base64Image);
        for (int i = 0; i < count; i++) {
            if (numLabels[i].getText().equals(name)){
                imageViews[i].setImage(image);
                labels[i].setText(createdAt);
                break;
            } else {
                continue;
            }
        }
        System.gc();
    }
    

    private void write2file(Image image, String base64Image) throws IOException {
        FileWriter fw = new FileWriter("record.txt", true);
        fw.write("Width: " + image.getWidth() +  
                 "\nHeight: " + image.getHeight() + 
                 "\nKB: " + (DECODER.decode(base64Image)).length/1024 +"\n=====");
        fw.close();
    }
    Base64.Decoder DECODER = Base64.getDecoder();
    
//    public void setSnapshots(List<Pair<String, Date>> snapshots) {
//        Collections.sort(snapshots, (a, b) -> {
//            return (int) (b.getValue().getTime() - a.getValue().getTime());
//        });
//        
//        try {
//            snapshots = snapshots.subList(0, numSnapshots);
//        } catch (IndexOutOfBoundsException e) {
//            LOGGER.log(Level.INFO, "size of snapshots({0}) is lower than {1}",
//                new Object[] {snapshots.size(), numSnapshots});
//        }
//
//        for (int i = 0; i < numSnapshots; i++) {
//            if (i < snapshots.size()) {
//                Image image = ImageUtils.decode(snapshots.get(i).getKey(), 
//                    imageViews[i].getFitWidth(), imageViews[i].getFitHeight());
//                
//                imageViews[i].setImage(image);
//                labels[i].setText(snapshots.get(i).getValue().toString());
//            } else {
//                // clear image view and label
//                imageViews[i].setImage(null);
//                labels[i].setText("");
//            }
//        }
//        
//        System.gc();
//    }
 
}
