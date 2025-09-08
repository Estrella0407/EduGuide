package com.example.eduguide.GraphModule;

import java.util.*;
import com.example.eduguide.Edge;
import com.example.eduguide.Login;

public class EnrollInCourse {
    private GraphOperations graph;
    private Map<String, List<Edge>> graphData;
    private Login login;
    private Map<String, Set<String>> studentEnrollments;

    public EnrollInCourse(GraphOperations graph, Login login, Map<String, Set<String>> studentEnrollments) {
        this.graph = graph;
        this.graphData = graph.getGraph();
        this.login = login;
        this.studentEnrollments = studentEnrollments;
    }

    public void enrollInCourse(Scanner scanner) {
        System.out.println("\nAvailable Courses:");
        for (String course : graphData.keySet()) {
            System.out.println("- " + course);
        }

        System.out.print("Enter the course code to enroll: ");
        String input = scanner.nextLine().trim();

        String courseKey = graph.findCourseKey(input);
        if (courseKey == null) {
            System.out.println("Course not found: " + input);
        } else {
            String studentId = login.getCurrentUserId();
            studentEnrollments.putIfAbsent(studentId, new HashSet<>());
            Set<String> myCourses = studentEnrollments.get(studentId);

            if (myCourses.contains(courseKey)) {
                System.out.println("You are already enrolled in " + courseKey + "!");
            } else {
                if (graph.canEnroll(courseKey, studentId, studentEnrollments)) {
                    myCourses.add(courseKey);
                    System.out.println("Successfully enrolled in " + courseKey + "!");
                    
                    // Save enrollments to file after successful enrollment
                    EnrollmentPersistence.saveEnrollments(studentEnrollments);
                } else {
                    System.out.println("Cannot enroll in " + courseKey + ". Prerequisites not met.");
                }
            }
        }

        System.out.println("\nPress Enter to return to menu...");
        scanner.nextLine();
    }
}