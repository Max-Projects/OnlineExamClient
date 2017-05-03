package main;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Yao
 */
public class UserStage {
    
    private SimpleDateFormat sdf;
    
    public FXMLLoader loadUserStage(String path){
        FXMLLoader fxmlLoader = new FXMLLoader();        
        
        try{
            Stage stage = new Stage();
            Pane root = fxmlLoader.load(getClass().getResource(path).openStream());
            
            stage.setScene(new Scene(root));
            stage.show();
            return fxmlLoader;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return fxmlLoader;
    }
    
    public void loadLoginStage(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/main/MainFXML.fxml"));
            Stage loginStage = new Stage();
           
            loginStage.setScene(new Scene(root));  
            loginStage.show(); 
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    
    public String getDateTime() {
        sdf = new SimpleDateFormat("yyyy年MM月dd日(星期E)    HH:mm:ss");
        DateFormatSymbols symbols = sdf.getDateFormatSymbols();
        symbols.setShortWeekdays(new String[] {"", "日", "一", "二", "三", "四", "五", "六"});
        sdf.setDateFormatSymbols(symbols);
        return sdf.format(new Date());
    }
    
    public int getHour() {
        Calendar calendar = GregorianCalendar.getInstance(); 
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour;
    }
}
