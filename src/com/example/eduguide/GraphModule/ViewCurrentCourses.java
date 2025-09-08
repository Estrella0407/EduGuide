package com.example.eduguide.GraphModule;

import java.util.*;

import com.example.eduguide.Login;

public class ViewCurrentCourses {

    private Login login;
    private Map<String, Set<String>> studentEnrollments;

    public ViewCurrentCourses(Login login, Map<String, Set<String>> studentEnrollments) {
        this.login = login;
        this.studentEnrollments = studentEnrollments;
    }

    public void display() {
        String studentId = login.getCurrentUserId();
        Set<String> enrolledCourses = studentEnrollments.getOrDefault(studentId, new HashSet<>());

        if (enrolledCourses.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
        } else {
            System.out.println("Your Current Enrolled Courses:");
            for (String course : enrolledCourses) {
                System.out.println("- " + course);
            }
        }
    }
}
