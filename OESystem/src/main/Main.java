package main;

import java.io.IOException;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.mislab.api.Config;
import static javafx.application.Application.launch;

/**
 *
 * @author Yao
 */
public class Main extends Application {

    @FXML
    TextField id;
    
    public static String args;
    
    /**
     * 
     * @param primaryStage 主要場景
     */
    @Override
    public void start(Stage primaryStage) {
        Parameters params = this.getParameters();
        Map<String, String> args = params.getNamed(); // get command line arguments 
        
//        Config.HOST = args.get(0);
        Config.ServerURL = "http://"+args.get("server")+":8000";
        Config.LocalURL = args.get("local");
        Config.PORT = 3001;
        Config.snapshotFreq = args.get("ssfreq");
        Config.snapshotScale = args.get("ssscale");
//        Config.LocalURL = args.get(1);
        
        System.out.println("ServerURL: ["+Config.ServerURL+"]");
        System.out.println("LocalURL: ["+Config.LocalURL+"]");
        System.out.println("SnapshotFreq: [" + Config.snapshotFreq +"]");
        System.out.println("SnapshotScale: [" + Config.snapshotScale + "]");
        
        try{
            // 載入fxml
            Parent root = FXMLLoader.load(getClass().getResource("/main/MainFXML.fxml"));
            Scene scene = new Scene(root);
            
            // 設定主場景之畫面
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
