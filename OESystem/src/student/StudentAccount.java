package student;

import org.mislab.api.Student;
import org.mislab.api.event.EventAction;
import org.mislab.api.event.EventType;
import org.mislab.api.event.OnlineExamEvent;
import org.mislab.test.event.UserAccount;

/**
 *
 * @author Max
 */
public class StudentAccount extends UserAccount {
    private String msgName, msg, action;
    private StudentController stStage;

    public StudentAccount(Student student) {
        super(student);
    }

    @Override
    public void setupEventListener() {
        evMgr.addEventListener(this, EventType.User, EventAction.Login);
        evMgr.addEventListener(this, EventType.User, EventAction.Logout);
        evMgr.addEventListener(this, EventType.Chat, EventAction.NewMessage);
        
        evMgr.addEventListener(this, EventType.Exam, EventAction.Halt);
        evMgr.addEventListener(this, EventType.Exam, EventAction.Extend);
        evMgr.addEventListener(this, EventType.Exam, EventAction.Pause);
        evMgr.addEventListener(this, EventType.Exam, EventAction.Resume);
        
        evMgr.addEventListener(this, EventType.Monitor, EventAction.Start);
        evMgr.addEventListener(this, EventType.Monitor, EventAction.Stop);
    }
    
    @Override
    public void handleOnlineExamEvent(OnlineExamEvent e) {
        try{
            String sname = (String) e.getContent().get("name");
            System.out.println(String.format("%s got an event %s from %s", this.getName(), e, sname));
            switch ((String) e.getAction().name()){
                case "NewMessage":
                    msgName = (String) e.getContent().get("name");
                    msg = (String) e.getContent().get("message");
                    System.out.println("*" + msgName + " : " + msg);
                    stStage.setMsgArea(msgName + " : " + msg);
                    break;
            }
        } catch (NullPointerException ex){
           // System.out.println(String.format("%s got an event %s", this.getName(), e));
            switch ((String) e.getAction().name()){
                case "Pause":
                    System.out.println("*Exam pause...");
                    stStage.pauseExam();
                    break;
                case "Resume":
                    System.out.println("*Exam resume...");
                    stStage.resumeExam();
                    break;
                case "Start":
                    stStage.startSnapshot();
                    break;
                case "Stop":
                    stStage.stopSnapshot();
                    break;
            }
            
        }              
    }
    
    public Student getExam(){
        return (Student)user;
    }
    
    public void setStage(StudentController ss){
        stStage = ss;
        stStage.setStudent(this);
    }
}
