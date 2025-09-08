package com.example.eduguide.GraphModule;

import java.util.*;

import com.example.eduguide.Edge;


public class GraphDisplay {
    private GraphOperations graphOperations;

    public void displayGraph(GraphOperations graph) {
        Map<String, List<Edge>> graphData = graph.getGraph();
        for (String vertex : graphData.keySet()) {
            System.out.println("\n" + vertex + " ->");
            for (Edge edge : graphData.get(vertex)) {
                System.out.println(" |__ " + edge.getRelation() + " -> " + edge.getToVertex());
            }
        }
    }

    public void recommendCourses(GraphOperations graph, String student, 
            Map<String, Set<String>> enrolledCourses) {
        
        Set<String> alreadyEnrolled = enrolledCourses.getOrDefault(student, new HashSet<>());
        List<String> canEnrollNow = new ArrayList<>();
        List<String> futureRecommendations = new ArrayList<>();
        

        Map<String, List<Edge>> graphData = graph.getGraph();

        // Check each course in the graph
        for (String course : graphData.keySet()) {
            if (alreadyEnrolled.contains(course)) {
                continue;  // Skip already enrolled courses
            }

            if (graphOperations.canEnroll(course, student, enrolledCourses)) {
                canEnrollNow.add(course);
            } else {
                futureRecommendations.add(course);
            }
        }

        // Display results
        System.out.println("Enrolled Courses for " + student + ":");
        for (String course : alreadyEnrolled) {
            System.out.println("   " + course);
        }

        System.out.println("\nCan Enroll Now:");
        for (String course : canEnrollNow) {
            System.out.println("   " + course);
        }

        System.out.println("\nFuture Recommendations:");
        for (String course : futureRecommendations) {
            System.out.println("   " + course);
        }
    }

    public void universityCourseNetwork(GraphOperations graph) {
        Map<String, List<Edge>> graphData = graph.getGraph();
        Map<String, Set<String>> prerequisites = new HashMap<>();
        
        // First pass: collect prerequisites for each course using the same logic as search
        for (String course : graphData.keySet()) {
            List<Edge> edges = graphData.get(course);
            if (edges != null) {
                for (Edge edge : edges) {
                    String relation = edge.getRelation().toLowerCase();
                    if (relation.contains("prerequisite")) {
                        prerequisites.computeIfAbsent(course, k -> new HashSet<>())
                                .add(edge.getToVertex());
                    }
                }
            }
        }
        
        // Display Available Courses with prerequisites
        System.out.println("\nAvailable Courses:");
        for (String course : graphData.keySet()) {
            Set<String> prereqs = prerequisites.get(course);
            if (prereqs == null || prereqs.isEmpty()) {
                System.out.println("   - " + course + " (No prerequisites)");
            } else {
                System.out.println("   - " + course + " (Prerequisites: " + 
                                String.join(", ", prereqs) + ")");
            }
        }
        
        // Display Pathways
        System.out.println("\nPathways:");
        displayGraph(graph);
    }
}
