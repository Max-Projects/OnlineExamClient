package teacher;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.UserStage;
import teacher.view.SnapshotPane;

/**
 * FXML Controller class
 *
 * @author Yao
 */
public class TeacherController extends UserStage implements Initializable{
    
    private static final Logger logger = Logger.getLogger(TeacherController.class.getSimpleName());
    
    private TeacherAccount teacher;
    private final Alert alert = new Alert(AlertType.CONFIRMATION);
    private Timeline timeline;
    private ListView listView = new ListView();
    private ObservableList<String> list = FXCollections.observableArrayList();    
    private SnapshotPane snapshot;
    private GridPane gridPane;
    private int examId, courseId;
    private List courses, exams;
    private int count;

    
    @FXML
    private Button logoutBtn;
    @FXML
    private TextField course_name;
    @FXML
    private TextField course_year;
    @FXML
    private TextField course_semester;
    @FXML
    private TextField exam_courseId;
    @FXML
    private TextField exam_name;
    @FXML
    private TextField exam_duration;
    @FXML
    private TextField courseIdText;
    @FXML
    private TextField problem_cid;
    @FXML
    private TextField problem_eid;
    @FXML
    private TextField problem_title;
    @FXML
    private TextArea msgInputArea;
    @FXML
    private TextArea msgArea;
    @FXML
    private TextArea problem_desc;
    @FXML
    private TextArea key_textArea;
    @FXML
    private Pane courseCreatePane;
    @FXML
    private Pane examCreatePane;
    @FXML
    private Pane examQueryPane;
    @FXML
    private Pane courseQueryPane;
    @FXML
    private Pane problemCreatePane;
    @FXML
    private Pane monitorPane;
    @FXML
    private Label courseQueryLabel;
    @FXML
    private Label examQueryLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private Label welcomeUserLabel;
    @FXML
    private Label pathLabel;
    @FXML
    private ScrollPane studentList;
    @FXML
    private ScrollPane snapshotPane;
    @FXML
    private Menu monitorMenu;

    public TeacherController() {
       // this.snapshot = new SnapshotPane();
    }
    

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
    
    public void setTeacher(TeacherAccount t) {
        List courses, exams;
        teacher = t;
        courses = ((List) teacher.getUser().queryCourses().getContent().get("courses"));
        exams = null;
        Menu courseItem = null;
        MenuItem examItem = null;
        for (int i = 0; i< courses.size(); i++){
            final int cid = Integer.parseInt(((Map)courses.get(i)).get("courseId").toString());
            final String courseName = ((Map)courses.get(i)).get("courseName").toString();
            exams = ((List) teacher.getUser().queryExams(cid).getContent().get("exams"));
            courseItem = new Menu(courseName);
            for (int j = 0; j< exams.size(); j++){
                final int eid = Integer.parseInt(((Map)exams.get(j)).get("examId").toString());
                final String examName = ((Map)exams.get(j)).get("examName").toString();
                examItem = new MenuItem(examName);
                examItem.setOnAction((ActionEvent event) -> {
                    courseId = cid;
                    examId = eid;
                    setMonitorPane(event);
                    setProblem(courseName, examName, courseId, examId);
                    System.out.println(courseId);
                    List st = ((List) teacher.getExam().queryStudents(courseId).getContent().get("students"));
                    snapshot = new SnapshotPane(st);
                });
                courseItem.getItems().add(examItem);
            }
            monitorMenu.getItems().add(courseItem);
        }
        
        if(getHour() < 12) {
            welcomeUserLabel.setText(teacher.getName() + " 早上好！");
        } else if(11 < getHour()  &&  getHour() < 18) {
            welcomeUserLabel.setText(teacher.getName() + " 下午好！");
        } else {
            welcomeUserLabel.setText(teacher.getName() + " 晚上好！");
        }  
    }
    
    //    登出
    public void logout(ActionEvent event){
        alert.setTitle("登出系統");
        alert.setHeaderText(""); 
        alert.setContentText("您真的要登出嗎？");
        final Optional<ButtonType> opt = alert.showAndWait();
        
        if (opt.get() == ButtonType.OK) {
            Stage teacherStage = (Stage) logoutBtn.getScene().getWindow();
            teacher.logout();
            loadLoginStage();
            teacherStage.close();
        } 
    }
    ArrayList<String> slist = new ArrayList<>();
    public void loadStudentList(String name, String id) {
        slist.add(id);
        Platform.runLater(() -> {
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            list.add(name);
            listView.setItems(list);
            
            MenuItem[] items = {new MenuItem("Snapshot"), new MenuItem("key")};
            
            items[0].setOnAction((ActionEvent event) -> {
//                loadSnapshots(id);
                  //loadSnapshots();
            });
            
            items[1].setOnAction((ActionEvent event) -> {
                setMsgArea("[系統測試]");
            });
            
            ContextMenu contextMenu = new ContextMenu(items);
            listView.setContextMenu(contextMenu);
            listView.setPrefSize(155, 425);
            studentList.setContent(listView);
        });
        
    }
    
    public void removeStudentFromList(String name) {
        Platform.runLater(() -> {
            listView.getItems().remove(name);
            gridPane = null;
            snapshotPane.setContent(gridPane);
        });
    }
    
    public void sendMsg(ActionEvent event){
        teacher.getUser().sendMessage(courseId, examId, msgInputArea.getText());
        msgInputArea.clear();
    }
   
//    考試暫停
    public void pauseExam(ActionEvent event) {
        teacher.getExam().pauseExam(courseId, examId);
    }
    
//    繼續考試
    public void resumeExam(ActionEvent event) {
        teacher.getExam().resumeExam(courseId, examId);
    }
    
    public void setMonitorPane(ActionEvent event) {
        monitorPane.setVisible(true);
    }
    
//    開始監考

    public void startMonitor(ActionEvent event) throws Exception {
        for (String s: slist) {
            teacher.getExam().startMonitor(courseId, examId, s);
        }
        setMsgArea("[系統]Start monitor.");
    }
    

//    停止監考
//    student id: 學號(e.g. 4026s), not id (e.g. 3, 1, or 2)
    public void stopMonitor(ActionEvent event) {
        for (String s: slist) {
            teacher.getExam().stopMonitor(courseId, examId, s);
        }    
        setMsgArea("[系統]Stop monitor.");
    }
    
//    載入截圖
    public void loadSnapshots(String sid, String name, String base64Image, String createdTimeStr) {
        Platform.runLater(() -> {
            Date createdAt;
            String time="";
            try {
                TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdTimeStr);
                TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd\nHH:mm:ss");
                time = sdf.format(createdAt);
            } catch (ParseException ex) {
                logger.log(Level.INFO, "cannot parse time string: {0}", createdTimeStr);
                createdAt = new Date();
            }
            
            try {
                snapshot.addSnapshot(name, base64Image, time);
            } catch (IOException ex) {
                Logger.getLogger(TeacherController.class.getName()).log(Level.SEVERE, null, ex);
            }
            snapshotPane.setContent(snapshot);
        });
    }

    public void setKeyEventLabel(String str) {
       key_textArea.appendText(String.valueOf(str));
    }
 
//    顯示訊息
    public void setMsgArea(String msg){
        msgArea.appendText(msg + "\n");
    }

//    新增課程OK - 顯示新增課程畫面
    public void setCreateCoursePane(ActionEvent event){
        courseCreatePane.setVisible(true);
    }
    
//    查詢課程OK
    public void queryCourse(ActionEvent event){
        String str = "";
        courseQueryPane.setVisible(true);
        int len= courses.size();
        
        for (int i = 0; i < len; i++) {
            str += "Course Name: " + 
                    ((Map)courses.get(i)).get("courseName").toString() + "\n";
        }
        courseQueryLabel.setText(str);
           //System.out.println(tch.getUser().queryCourses().getContent().toString());
    }
    
//    新增課程 - 送出鈕，同時新增至資料庫
    public void sendCourse(ActionEvent event){
        List<String> studentIds = new ArrayList<String>() {{
            add("4025s");
        }};
        teacher.getExam().createCourse(course_name.getText(), Integer.parseInt(course_year.getText()), 
                Integer.parseInt(course_semester.getText()), studentIds);
        courseCreatePane.setVisible(false);
    }
    
//    新增考試OK - 顯示新增考試畫面
    public void setCreateExamPane(ActionEvent event){
        examCreatePane.setVisible(true);
    }
    
//    查詢考試OK - 顯示查詢畫面
    public void queryExam(ActionEvent event){
        examQueryPane.setVisible(true);
    }
    
//    查詢考試OK - 送出鈕，同時從資料庫抓取資料並顯示
    public void sendQueryExam(){
        String str = "";

        int len= exams.size();
        
        for (int i = 0; i < len; i++) {
            str += "Name: " + 
                ((Map)exams.get(i)).get("examName").toString() + "\n";
        }
        examQueryLabel.setText(str);        
    }
    
//    新增考試OK - 送出鈕，同時新增至資料庫
    public void sendExam(ActionEvent event){
        Date date = new Date();       
        Timestamp timestamp = new Timestamp(date.getTime());
        teacher.getExam().createExam(Integer.parseInt(exam_courseId.getText()), exam_name.getText(), timestamp, Integer.parseInt(exam_duration.getText()));
        examCreatePane.setVisible(false);
    }

//    新增題目 - 顯示新增題目畫面
    public void createProblem(ActionEvent event){
        problemCreatePane.setVisible(true);
    }
    
//    新增題目OK - 送出鈕，同時新增至資料庫
    public void sendProblem(ActionEvent event){
        int cid = Integer.parseInt(problem_cid.getText());
        int eid = Integer.parseInt(problem_eid.getText());
        org.mislab.api.Problem problem = new org.mislab.api.Problem(problem_title.getText(),  problem_desc.getText());
        problem.addTestData("input for p1.1", "output1.1");
        problem.addTestData("input for p1.2", "output222");
        
        teacher.getExam().createProblem(cid, eid, problem);
        problemCreatePane.setVisible(false);
    }
    
//    目前假設每一個exam只有一個problem
    private void setProblem(String courseName, String examName, int courseId, int examId) {
        pathLabel.setText(courseName + " -> " +examName);
        List problems = (List) teacher.getExam().queryProblems(courseId, examId).getContent().get("problems");
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
    
//    關閉畫面（課程查詢、考試查詢）
    public void close(ActionEvent event){
        courseQueryPane.setVisible(false);
        examQueryPane.setVisible(false);
        courseCreatePane.setVisible(false);
        examCreatePane.setVisible(false);
        problemCreatePane.setVisible(false);
        monitorPane.setVisible(false);
    }
  
}