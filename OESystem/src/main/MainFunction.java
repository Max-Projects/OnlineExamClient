/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.mislab.api.Response;
import org.mislab.api.Role;
import org.mislab.api.Student;
import org.mislab.api.Teacher;
import org.mislab.api.User;



/**
 *
 * @author Yao
 */
public class MainFunction {
    private Student student;
    private Teacher teacher;

    public void registerStudent() {
        Response res = User.register("student", "student", "廖同學", Role.Student,
                "4026s", "scott@student.com", 104, new byte[5]);
    }

    public void registerTeacher() {
        Response res = User.register("teacher", "teacher", "李老師", Role.Teacher,
                "peter", "peter@teacher.com", 104, new byte[5]);
    }

    public Student loginStudent(String userName, String psw) {
        Response res = User.login(userName, psw);

        if (res.success()) {
            student = (Student) res.getContent().get("user");
        }else{
            System.out.println("ErrorCode: " + res.getErrorCode());
        }
        return student;
    }

    public Teacher loginTeacher() {
        Response res = User.login("teacher", "teacher");

        if (res.success()) {
            teacher = (Teacher) res.getContent().get("user");
        }else{
            System.out.println("ErrorCode: " + res.getErrorCode());
        }
        
        return teacher;
    }
    
}
