package main; //名稱 user interface 主畫面

import teacher.TeacherAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mislab.api.Response;
import org.mislab.api.Student;
import org.mislab.api.Teacher;
import org.mislab.api.User;
import student.StudentAccount;
import student.StudentController;
import teacher.TeacherController;



/**
 * FXML Controller class
 *
 * @author Yao
 */
public class MainController{
    UserStage userStage = new UserStage();
    
    @FXML
    TextField id;
    @FXML
    PasswordField pw;
    @FXML
    Label login_tip;
    @FXML
    Button loginBtn;
     
    public void loginAction(ActionEvent event) {
        String fxmlPath;
        FXMLLoader fxmlLoader;
        try {
            Response res = User.login(id.getText(), pw.getText());
            
            if (res.success()){
                String s = res.getContent().get("role").toString();
                
                switch (s) { 
                    case "teacher":
                        TeacherAccount tch = new TeacherAccount((Teacher)res.getContent().get("user"));
        //                登入畫面轉換至老師使用介面
                        fxmlPath = "/teacher/TeacherFXML.fxml";
                        fxmlLoader= userStage.loadUserStage(fxmlPath);
        //                取得fxml檔相對應之controller （fx:controller="teacher.TeacherController"）
                        TeacherController tchController =  (TeacherController)fxmlLoader.getController();
        //                登入並設定所登入之身份
                        tch.setController(tchController);
                        break;
                    case "student":
                        StudentAccount st = new StudentAccount((Student)res.getContent().get("user"));
        //                登入畫面轉換至學生使用介面     
                        fxmlPath = "/student/StudentFXML.fxml";
                        fxmlLoader= userStage.loadUserStage(fxmlPath);
                        StudentController stStage = (StudentController)fxmlLoader.getController();
                        st.setStage(stStage);
                }
            } else {
                throw new Exception();
            }
            Stage loginStage = (Stage) loginBtn.getScene().getWindow();
            loginStage.close();
        } catch (Exception ex) {
            login_tip.setText("Your user name/password is not correct. Please try again.");
            System.out.println("User name/password is not correct.");
        }
    }
    
    public void exitSystem(ActionEvent event) {
        System.exit(0);
    }
        
}
