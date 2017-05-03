package student;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.mislab.api.Student;

/**
 *
 * @author Yao
 */

public class ScreenSnapshot {
    private SnapshotProcessor image;
    private ExecutorService application = Executors.newCachedThreadPool();
    private boolean isSnapshot = false; //用來判斷是否在截圖階段
    private final Student student;
    private final int courseId;
    private final int examId;
    private final String studentId;
    
    public ScreenSnapshot(Student st, int cid, int eid, String sid) {
        student = st;
        courseId = cid;
        examId = eid;
        studentId = sid;
    }
    
//    開始截圖
    public void startSnapShot() {
        isSnapshot = true;
        image = new SnapshotProcessor(student, courseId, examId, studentId);
        application.execute(image);
    }
     
//    停止截圖
    public void stopSnapShot() {
        if (isSnapshot != false) {
            image.stop();
            isSnapshot = false;  
        } else {
            System.out.println("Cannot stop!");
        }
    }    
    
//    即時單次截圖（送出答案時執行）
    public void snapshotScreen(){
        try {
            image.snapshotScreen(image.getDateTime());
        } catch(Exception e) {
            System.out.println("*** Exception!!! ***\n" + e.getMessage()
                             + "\n******************");
        }
    }
}
