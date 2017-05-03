/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.util.List;
import java.util.Map;
import org.mislab.api.Response;
import org.mislab.api.Student;

/**
 *
 * @author Yao
 */
public class QuestionSet {
    private String title;
    private String question;
    
    public QuestionSet(Student st, int cid, int eid){
        setQuestion(st, cid, eid);
    }
    
    private void setQuestion(Student st, int cid, int eid){
        Response problem = st.queryProblems(cid, eid);
        title = ((Map) ((List) problem.getContent().get("problems")).get(0)).get("problemName").toString();
        question = ((Map) ((List) problem.getContent().get("problems")).get(0)).get("description").toString();
    }
    
    public String getQuestion() {
        return "題目：" + title + "\n內容：" + question;
    }
}
