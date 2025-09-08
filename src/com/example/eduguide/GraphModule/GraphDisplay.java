package com.example.eduguide.GraphModule;

import java.util.*;

import com.example.eduguide.Edge;


public class GraphDisplay {
    public void displayGraph(GraphOperations graph) {
        Map<String, List<Edge>> graphData = graph.getGraph();
        for (String vertex : graphData.keySet()) {
            System.out.println("\n" + vertex);
            for (Edge edge : graphData.get(vertex)) {
                System.out.println(" |__ " + edge.getRelation() + " -> " + edge.getToVertex());
            }
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
