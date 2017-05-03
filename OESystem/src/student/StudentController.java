package student;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.UserStage;
import org.mislab.api.Student;

/**
 * FXML Controller class
 *
 * @author Yao
 */
public class StudentController extends UserStage implements Initializable{
    
    private StudentAccount student;
    private ScreenSnapshot screenSnapshot;
    private Timeline timeline;   
    private boolean snapshotFlag = false;
    private Alert alert;
    private List courses, exams;
    private final int examId = 1, courseId = 2;
    private MenuItem courseItem;
    
    @FXML
    private Button logoutBtn;
    @FXML
    private TextArea msgInputArea;
    @FXML
    private TextArea msgArea;
    @FXML
    private TextArea answerArea;
    @FXML
    private Pane examPane;
    @FXML
    private Label timeLabel;
    @FXML
    private Label welcomeUserLabel;
    @FXML
    private Label pathLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private MenuButton menuBtn;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        Duration duration = Duration.millis(1000);
        KeyFrame keyFrame = new KeyFrame(duration, (ActionEvent event) -> {
            timeLabel.setText(getDateTime()); 
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
    
    public void setStudent(StudentAccount st) {
        student = st;

        courseItem = new MenuItem("開始考試");
        courseItem.setOnAction((ActionEvent event) -> {
                    startExam(event);
                    setProblem(courseId, examId);
                });
        courseItem.setDisable(false);
        menuBtn.getItems().add(courseItem);

        if(getHour() < 12) {
            welcomeUserLabel.setText(student.getName() + " 早上好！");
        } else if(11 < getHour()  &  getHour() < 18) {
            welcomeUserLabel.setText(student.getName() + " 下午好！");
        } else {
            welcomeUserLabel.setText(student.getName() + " 晚上好！");
        }
    }
    
    public void sendMsg(ActionEvent event){
        student.getUser().sendMessage(courseId, examId, msgInputArea.getText());
        setMsgArea(student.getUser().getProfile().getContent().get("name").toString()+ " : " + msgInputArea.getText());
         msgInputArea.clear();
    }
   
//  登出
    public void logout(ActionEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("登出系統");
        alert.setHeaderText(""); 
        alert.setContentText("您真的要登出嗎？\n!!如正在進行考試中選擇登出，並且作答尚未完成或交卷，將一律視同交卷!!");
        final Optional<ButtonType> opt = alert.showAndWait();
        
        try {
            if (student != null & opt.get() == ButtonType.OK) {
                if (snapshotFlag == true) {
                    stopSnapshot();
                    snapshotFlag = false;
                } 
                Stage studnetStage = (Stage) logoutBtn.getScene().getWindow();
                student.logout();
                student = null;
                System.out.println("Logout success.");
                loadLoginStage();
                studnetStage.close();
            } else {
                System.out.println("Canceled!");
            }
        } catch (Exception ioe) {
            System.out.println("\n*** IOException!!! ***");
        }
    }
    
    public void setMsgArea(String msg){
        msgArea.appendText(msg + "\n");
    }
    
//    考試暫停 _ 禁止編輯作答區域
    public void pauseExam() {
        msgInputArea.setEditable(false);
        msgInputArea.setStyle("-fx-control-inner-background: lightgray");
        setMsgArea("[系統] Exam pause");
    }
    
//    恢復考試 _ 開放編輯作答區域
    public void resumeExam() {
        msgInputArea.setEditable(true);
        msgInputArea.setStyle(null);
        setMsgArea("[系統] Exam resume");
     }
    
//    偵測鍵盤按下的字元
    public void keyEvent(KeyEvent e) {
        student.getExam().sendKeyEvent(courseId, examId, e);
       // System.out.println(e.getCode().ordinal()+" : " +e.getEventType().getName());
    }

//    開始截圖   
    public void startSnapshot() {
        screenSnapshot = new ScreenSnapshot((Student)student.getUser(), courseId, examId, student.getId());
        screenSnapshot.startSnapShot();
        snapshotFlag = true;
    }
     
//    停止截圖   
    public void stopSnapshot() {
         screenSnapshot.stopSnapShot();
         snapshotFlag = false;
    }    
     
    public void startExam(ActionEvent event) {
        if(courseItem.isDisable() == true) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(""); 
            alert.setContentText("您已交卷或考試尚未開始");
            final Optional<ButtonType> opt = alert.showAndWait();
        } else {
            examPane.setVisible(true);
            student.getExam().attendExam(courseId, examId);
        }
    }
     
    public void closeExamPane(ActionEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("關閉考試視窗");
        alert.setHeaderText(""); 
        alert.setContentText("您真的要關閉考試視窗嗎？\n如尚未作答完成或交卷，就關閉此視窗\n!!將一律視同交卷!!");
        final Optional<ButtonType> opt = alert.showAndWait();
        if (opt.get() == ButtonType.OK) {
            examPane.setVisible(false);
            courseItem.setDisable(true);
        }
    }
    
    //    目前假設每一個exam只有一個problem
    private void setProblem(int courseId, int examId) {
       // pathLabel.setText(courseName + " -> " +examName);
        List problems = (List) student.getExam().queryProblems(courseId, examId).getContent().get("problems");
        questionLabel.setText("Title: " + ((Map)problems.get(0)).get("problemName").toString() +
                "\n-----------\nDesc.: " + ((Map)problems.get(0)).get("description").toString());
        
//       當一個exam有多個problem
//        switch(problems.size()) {
//            case 1:
//                questionLabel.setText(((Map)problems.get(0)).get("problemName").toString());
//                break;
//            default:
//                for (int i = 0; i < problems.size(); i++){
//                    problemStr += ((Map)problems.get(i)).get("problemName").toString() + "\n";
//                }
//                questionLabel.setText(problemStr);
//        }
    }
    
    public void submitAnswer(ActionEvent event) {
    }
}
