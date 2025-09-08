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
        
        // First pass: collect prerequisites for each course
        for (String course : graphData.keySet()) {
            for (Edge edge : graphData.get(course)) {
                if (edge.getRelation().equalsIgnoreCase("hasPrerequisite")) {
                    prerequisites.computeIfAbsent(course, k -> new HashSet<>())
                               .add(edge.getToVertex());
                }
            }
        }
        
        // Display Available Courses with prerequisites
        System.out.println("\nAvailable Courses:");
        for (String course : graphData.keySet()) {
            if (!prerequisites.containsKey(course) || prerequisites.get(course).isEmpty()) {
                System.out.println("   - " + course + " (No prerequisites)");
            } else {
                System.out.println("   - " + course + " (Prerequisite of " + 
                                 String.join(", ", prerequisites.get(course)) + ")");
            }
        }
        
        // Display Pathways
        System.out.println("\nPathways:");
        Set<String> startingCourses = findStartingCourses(graphData);
        Set<String> visited = new HashSet<>();
        
        for (String startCourse : startingCourses) {
            if (!visited.contains(startCourse)) {
                List<String> path = new ArrayList<>();
                displayPath(graphData, startCourse, path, visited);
            }
        }
    }

    private Set<String> findStartingCourses(Map<String, List<Edge>> graphData) {
        Set<String> allCourses = new HashSet<>(graphData.keySet());
        Set<String> hasPrerequisites = new HashSet<>();
        
        // Find courses that are prerequisites
        for (List<Edge> edges : graphData.values()) {
            for (Edge edge : edges) {
                if (edge.getRelation().equalsIgnoreCase("hasPrerequisite")) {
                    hasPrerequisites.add(edge.getToVertex());
                }
            }
        }
        
        // Remove courses that have prerequisites
        allCourses.removeAll(hasPrerequisites);
        return allCourses;
    }

    private void displayPath(Map<String, List<Edge>> graphData, String current, 
                            List<String> path, Set<String> visited) {
        path.add(current);
        visited.add(current);
        
        // Get next courses in the pathway
        List<String> nextCourses = new ArrayList<>();
        for (Edge edge : graphData.get(current)) {
            if (edge.getRelation().equalsIgnoreCase("hasPrerequisite")) {
                nextCourses.add(edge.getToVertex());
            }
        }
        
        if (nextCourses.isEmpty()) {
            // Print the complete path when we reach the end
            System.out.println("   " + String.join(" -> ", path));
        } else {
            // Continue with each branch
            for (String nextCourse : nextCourses) {
                if (!path.contains(nextCourse)) { // Avoid cycles
                    displayPath(graphData, nextCourse, new ArrayList<>(path), visited);
                }
            }
        }
    }
}
