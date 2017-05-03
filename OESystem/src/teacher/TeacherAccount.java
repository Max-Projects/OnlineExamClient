package teacher;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import org.mislab.api.Teacher;
import org.mislab.api.event.EventAction;
import org.mislab.api.event.EventType;
import org.mislab.api.event.OnlineExamEvent;
import org.mislab.test.event.UserAccount;

/**
 *
 * @author Max
 */
public class TeacherAccount extends UserAccount {
        
    private String sname, msg, action, keyEvent;
    private TeacherController tchController;
    
    public TeacherAccount(Teacher teacher)  {
        super(teacher);
    }
      
    @Override
    public void setupEventListener() {
        evMgr.addEventListener(this, EventType.User, EventAction.Login);
        evMgr.addEventListener(this, EventType.User, EventAction.Logout);
        evMgr.addEventListener(this, EventType.Chat, EventAction.NewMessage);
        
        evMgr.addEventListener(this, EventType.Exam, EventAction.Attend);
        evMgr.addEventListener(this, EventType.Exam, EventAction.Halt);
        evMgr.addEventListener(this, EventType.Exam, EventAction.Extend);
        evMgr.addEventListener(this, EventType.Exam, EventAction.Pause);
        evMgr.addEventListener(this, EventType.Exam, EventAction.Resume);
        
        evMgr.addEventListener(this, EventType.Monitor, EventAction.Start);
        evMgr.addEventListener(this, EventType.Monitor, EventAction.Stop);
        evMgr.addEventListener(this, EventType.Monitor, EventAction.RequestSnapshot);
        evMgr.addEventListener(this, EventType.Monitor, EventAction.SendSnapshot);
        
        evMgr.addEventListener(this, EventType.Monitor, EventAction.KeyEvent);
    }
    
    @Override
    public void handleOnlineExamEvent(OnlineExamEvent event) {
        try{
            sname = event.getContent().get("name");
            action = event.getAction().name();
            System.out.println(String.format("%s got an event %s from %s", this.getName(), event, sname));
            switch (action){
                case "Login":
                    System.out.println("*" + sname + " " + action);
                    tchController.setMsgArea("[系統] " + sname + " " + action);
                    break;
                case "Logout":
                    System.out.println("*" + sname + " " + action);
                    tchController.setMsgArea("[系統] " + sname + " " + action);
                    tchController.removeStudentFromList(sname);
                    break;   
                case "Attend":
                    System.out.println("*" + sname + " " + action);
                    tchController.setMsgArea("[系統] " + sname + " Start Exam");
                    tchController.loadStudentList(sname, event.getContent().get("id"));
                    break;
                case "NewMessage":
                    msg = event.getContent().get("message");
                    System.out.println("*" + sname + " : " + msg);
                    tchController.setMsgArea(sname + " : " + msg);
                    break;
                case "SendSnapshot":                    
                    String studentId = event.getContent().get("studentId");
                    String base64Image = event.getContent().get("snapshot");
                    String createTimeStr = event.getContent().get("time");
                    
                    write2file(studentId + " " + sname);
                    tchController.loadSnapshots(studentId, sname, base64Image, createTimeStr);
                    break;
                case "KeyEvent":
                    keyEvent = (String) event.getContent().get("keyChar");
                    tchController.setKeyEventLabel(keyEvent);
                    break;
            }
        } catch (NullPointerException npe){
//            System.out.println(String.format("%s got an event %s", this.getName(), event));
            switch (action){
                case "Pause":
                    System.out.println("*Exam pause...");
                    tchController.setMsgArea("[系統] Exam pause");
                    break;
                case "Resume":
                    System.out.println("*Exam resume...");
                    tchController.setMsgArea("[系統] Exam resume");
                    break;  
            }
        } catch (IOException ex) {
            Logger.getLogger(TeacherAccount.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public Teacher getExam() {  
        return  (Teacher)user; 
    }
    
    public void setController(TeacherController tc) {
        tchController = tc;
        tchController.setTeacher(this);
    }
    
    private String getTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");
        Date date = new Date();
        String strDate = "Time: " + sdFormat.format(date);
        return strDate;
    } 
    
    private void write2file(String name) throws IOException {
        
        FileWriter fw = new FileWriter("record.txt", true);
        fw.write("=====\n" + "Name: " + name + "\n"+  getTime() + "\n");
        fw.close();
    }
    
    
}