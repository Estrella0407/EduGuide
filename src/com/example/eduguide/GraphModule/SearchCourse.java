package com.example.eduguide.GraphModule;

import java.util.*;

import com.example.eduguide.Edge;

public class SearchCourse {
    public void searchCourse(Scanner scanner) {
        GraphOperations graph = new GraphOperations();
        Map<String, List<Edge>> graphData = graph.getGraph();

        System.out.print("Enter course code to search: ");
        String query = scanner.nextLine().trim().toLowerCase().replaceAll("\\s+", "");

        List<String> results = new ArrayList<>();
        for (String course : graphData.keySet()) {
            String normalizedCourse = course.toLowerCase().replaceAll("\\s+", "");
            if (normalizedCourse.contains(query)) {
                results.add(course);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No course found matching: " + query);
        } else {
            System.out.println("\nMatches:");
            for (String course : results) {
                System.out.println("Course: " + course);
                List<Edge> edges = graphData.get(course);

                if (edges != null && !edges.isEmpty()) {
                    List<String> prereqs = new ArrayList<>();
                    List<String> otherRelations = new ArrayList<>();

                    // Separate prerequisites and other relations
                    for (Edge edge : edges) {
                        String relation = edge.getRelation().toLowerCase();
                        if (relation.contains("prerequisite")) {
                            prereqs.add(edge.getToVertex());
                        } else {
                            otherRelations.add(edge.getRelation() + " -> " + edge.getToVertex());
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
                } else {
                    System.out.println("   - No prerequisites.");
                    System.out.println("   - No other relations.");
                }
            }
        }
    }
}
