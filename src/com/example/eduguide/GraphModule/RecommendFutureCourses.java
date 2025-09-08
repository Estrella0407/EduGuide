package com.example.eduguide.GraphModule;

import java.util.*;

import com.example.eduguide.Edge;
import com.example.eduguide.Login;

public class RecommendFutureCourses {

    private Login login;
    private Map<String, Set<String>> studentEnrollments;
    private GraphOperations graph;

    public RecommendFutureCourses(Login login, Map<String, Set<String>> studentEnrollments, GraphOperations graph) {
        this.login = login;
        this.studentEnrollments = studentEnrollments;
        this.graph = graph;
    }

    public void display() {
        String studentId = login.getCurrentUserId();
        Set<String> enrolledCourses = studentEnrollments.getOrDefault(studentId, new HashSet<>());
        
        if (enrolledCourses.isEmpty()) {
            System.out.println("No enrollments found. Please enroll in a course first.");
            return;
        }

        System.out.println("Recommended Future Courses:");
        Set<String> recommendations = new HashSet<>();
        
        for (String course : enrolledCourses) {
            List<Edge> edges = graph.getEdges(course); // if this method exists
            if (edges != null) {
                for (Edge edge : edges) {
                    String relation = edge.getRelation().toLowerCase();
                    // Check for any relation that indicates this course leads to another
                    if (relation.contains("prerequisite")) {
                        String nextCourse = edge.getToVertex();
                        if (!enrolledCourses.contains(nextCourse)) {
                            recommendations.add(nextCourse);
                        }
                    }
                }
            }
        }
        
        if (recommendations.isEmpty()) {
            System.out.println("No future course recommendations available at this time.");
        } else {
            for (String rec : recommendations) {
                System.out.println("- " + rec);
            }
        }
    }
}
