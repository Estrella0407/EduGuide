package com.example.eduguide.GraphModule;
import java.util.*;
import com.example.eduguide.Edge;

public class SearchCourse {
    private GraphOperations graph = new GraphOperations();
    
    // Helper method to normalize strings
    private String norm(String input) {
        return input.toLowerCase().replaceAll("\\s+", "").trim();
    }
    
    // Reusable search method - finds matching courses
    public List<String> searchCourses(String query) {
        String q = norm(query);
        List<String> results = new ArrayList<>();
        Map<String, List<Edge>> graphData = graph.getGraph();
        
        for (String key : graphData.keySet()) {
            if (norm(key).contains(q)) {
                results.add(key);
            }
        }
        Collections.sort(results);
        return results;
    }
    
    // Display method - handles user interaction and output
    public void searchCourse(Scanner scanner) {
        System.out.print("Enter course code to search: ");
        String query = scanner.nextLine().trim();
        
        List<String> results = searchCourses(query); // Use the reusable method
        
        if (results.isEmpty()) {
            System.out.println("No course found matching: " + query);
        } else {
            System.out.println("\nMatches:");
            displayCourseDetails(results);
        }
    }
    
    // Helper method to display course details
    private void displayCourseDetails(List<String> courses) {
        Map<String, List<Edge>> graphData = graph.getGraph();
        
        for (String course : courses) {
            System.out.println("Course: " + course);
            
            // Find prerequisites (courses that point TO this course with prerequisite relation)
            List<String> prereqs = new ArrayList<>();
            List<String> otherRelations = new ArrayList<>();
            
            // Search through all courses to find what points to the current course
            for (String fromCourse : graphData.keySet()) {
                List<Edge> edges = graphData.get(fromCourse);
                if (edges != null) {
                    for (Edge edge : edges) {
                        if (edge.getToVertex().equals(course)) {
                            String relation = edge.getRelation().toLowerCase();
                            if (relation.contains("prerequisite")) {
                                prereqs.add(fromCourse);
                            } else {
                                otherRelations.add(fromCourse + " " + edge.getRelation() + " -> " + course);
                            }
                        }
                    }
                }
            }
            
            // Also show what this course leads to
            List<Edge> outgoingEdges = graphData.get(course);
            if (outgoingEdges != null) {
                for (Edge edge : outgoingEdges) {
                    String relation = edge.getRelation().toLowerCase();
                    if (!relation.contains("prerequisite")) {
                        otherRelations.add(course + " " + edge.getRelation() + " -> " + edge.getToVertex());
                    }
                }
            }
            
            // Print prerequisites
            if (prereqs.isEmpty()) {
                System.out.println("   - No prerequisites.");
            } else {
                System.out.println("   - Prerequisite(s): " + String.join(", ", prereqs));
            }
            
            // Print other relations
            if (otherRelations.isEmpty()) {
                System.out.println("   - No other relations.");
            } else {
                System.out.println("   - Other relation(s):");
                for (String rel : otherRelations) {
                    System.out.println("     |__ " + rel);
                }
            }
        }
    }
}