package com.example.eduguide.GraphModule;

import java.io.*;
import java.util.*;
import com.example.eduguide.Edge;

public class GraphOperations {
    private Map<String, List<Edge>> graph;
    private static final String GRAPH_FILE = "graph_data.txt";

    public List<Edge> getEdges(String vertex) {
        return graph.get(vertex);
    }

    public Map<String, List<Edge>> getGraph() {
        return graph;
    }

    public void saveGraph() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(GRAPH_FILE))) {
            // Write number of vertices
            writer.println(graph.size());
            
            // Write each vertex and its edges
            for (Map.Entry<String, List<Edge>> entry : graph.entrySet()) {
                String vertex = entry.getKey();
                List<Edge> edges = entry.getValue();
                
                // Write vertex
                writer.println(vertex);
                
                // Write number of edges
                writer.println(edges.size());
                
                // Write each edge
                for (Edge edge : edges) {
                    writer.println(edge.getToVertex());
                    writer.println(edge.getRelation());
                }
            }
            System.out.println("Graph saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving graph: " + e.getMessage());
        }
    }

    public void loadGraph() {
        File file = new File(GRAPH_FILE);
        
        // If file doesn't exist, just initialize empty graph
        if (!file.exists() || file.length() == 0) {
            graph = new HashMap<>();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            graph = new HashMap<>(); // Initialize fresh HashMap instead of clear()
            
            String line = reader.readLine();
            if (line == null) {
                return; // Empty file
            }
            
            // Read number of vertices
            int vertexCount = Integer.parseInt(line.trim());
            
            // Read each vertex and its edges
            for (int i = 0; i < vertexCount; i++) {
                String vertex = reader.readLine();
                if (vertex == null) break;
                
                String edgeCountStr = reader.readLine();
                if (edgeCountStr == null) break;
                
                int edgeCount = Integer.parseInt(edgeCountStr.trim());
                List<Edge> edges = new ArrayList<>();
                
                // Read edges
                for (int j = 0; j < edgeCount; j++) {
                    String toVertex = reader.readLine();
                    String relation = reader.readLine();
                    if (toVertex == null || relation == null) break;
                    
                    edges.add(new Edge(toVertex, relation));
                }
                
                graph.put(vertex, edges);
            }
            System.out.println("Graph loaded successfully!");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading graph: " + e.getMessage());
            graph = new HashMap<>(); // Initialize empty graph on error
        }
    }

    public GraphOperations() {
        graph = new HashMap<>();
        loadGraph(); // Load saved graph when creating new instance
    }

    public void addVertex(String vertex) {
        // Standardize the input to uppercase
        String standardizedVertex = vertex.trim().toUpperCase();
        if (!graph.containsKey(standardizedVertex)) {
            graph.put(standardizedVertex, new ArrayList<>());
            System.out.println("Vertex '" + standardizedVertex + "' added successfully.");
        } else {
            System.out.println("Vertex '" + standardizedVertex + "' already exists!");
        }
    }

    public void addEdge(String fromVertex, String toVertex, String relation) {
        // Standardize the input to uppercase
        String standardizedFrom = fromVertex.trim().toUpperCase();
        String standardizedTo = toVertex.trim().toUpperCase();
        String standardizedRelation = relation.trim().toLowerCase();
        
        // Rest of your addEdge method remains the same...
        if (graph.containsKey(standardizedFrom)) {
            for (Edge edge : graph.get(standardizedFrom)) {
                if (edge.getToVertex().equals(standardizedTo) && 
                    edge.getRelation().equals(standardizedRelation)) {
                    System.out.println("This edge already exists!");
                    return;
                }
            }
        }
        
        // Add vertices if they don't exist
        addVertex(standardizedFrom);
        addVertex(standardizedTo);
        
        // Add the edge
        graph.get(standardizedFrom).add(new Edge(standardizedTo, standardizedRelation));
        System.out.println("Edge added successfully.");
    }

    public void removeEdge(String fromVertex, String toVertex) {
        String standardizedFrom = fromVertex.trim().toUpperCase();
        String standardizedTo = toVertex.trim().toUpperCase();
        
        if (graph.containsKey(standardizedFrom)) {
            graph.get(standardizedFrom).removeIf(edge -> edge.getToVertex().equals(standardizedTo));
        }
    }

    public void removeVertex(String vertex) {
        String standardizedVertex = vertex.trim().toUpperCase();
        graph.remove(standardizedVertex);
        
        // Also remove edges pointing to this vertex
        for (List<Edge> edges : graph.values()) {
            edges.removeIf(edge -> edge.getToVertex().equals(standardizedVertex));
        }
    }

    public void createGraphMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nGraph Menu:");
            System.out.println("1. Add Vertex");
            System.out.println("2. Add Edge");
            System.out.println("3. Remove Vertex");
            System.out.println("4. Remove Edge");
            System.out.println("5. Display Graph");
            System.out.println("6. Exit Graph Menu");
            System.out.print("Enter your choice: ");
            
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter vertex name: ");
                    String vertex = scanner.nextLine();
                    addVertex(vertex);
                    saveGraph(); // Save after adding vertex
                    break;
                case 2:
                    System.out.print("Enter from vertex: ");
                    String fromVertex = scanner.nextLine();
                    System.out.print("Enter to vertex: ");
                    String toVertex = scanner.nextLine();
                    System.out.print("Enter relation: ");
                    String relation = scanner.nextLine();
                    addEdge(fromVertex, toVertex, relation);
                    saveGraph(); // Save after adding edge
                    break;
                case 3:
                    System.out.print("Enter vertex name to remove: ");
                    String vertexToRemove = scanner.nextLine();
                    removeVertex(vertexToRemove);
                    System.out.println("Vertex removed.");
                    saveGraph(); // Save after removing vertex
                    break;
                case 4:
                    System.out.print("Enter from vertex: ");
                    String fromV = scanner.nextLine();
                    System.out.print("Enter to vertex: ");
                    String toV = scanner.nextLine();
                    removeEdge(fromV, toV);
                    System.out.println("Edge removed.");
                    saveGraph(); // Save after removing edge
                    break;
                case 5:
                    GraphDisplay graphDisplay = new GraphDisplay();
                    graphDisplay.displayGraph(this);
                    break;
                case 6:
                    System.out.println("Exiting Graph Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 6);
            
            saveGraph(); // Save before exiting
    }

    // Normalize: ignore spaces/underscores and case
    private String norm(String s) {
        return s == null ? "" : s.replaceAll("[\\s_]+", "").toUpperCase(Locale.ROOT);
    }

    /** Find the actual stored course key matching user input (case/space-insensitive). */
    public String findCourseKey(String userInput) {
        String target = norm(userInput);
        for (String key : graph.keySet()) {
            if (norm(key).equals(target)) return key;
        }
        return null;
    }

    /** Case/space-insensitive substring search over course keys. */
    public List<String> searchCourses(String query) {
        String q = norm(query);
        List<String> results = new ArrayList<>();
        for (String key : graph.keySet()) {
            if (norm(key).contains(q)) results.add(key);
        }
        Collections.sort(results);
        return results;
    }
    
    public List<String> getPrerequisites(String courseCode) {
        List<String> prerequisites = new ArrayList<>();
        
        // Look through ALL courses to find edges that point TO this course
        for (Map.Entry<String, List<Edge>> entry : graph.entrySet()) {
            String fromCourse = entry.getKey();
            List<Edge> edges = entry.getValue();
            
            for (Edge edge : edges) {
                // If this edge points to our target course
                if (edge.getToVertex().equals(courseCode)) {
                    String relation = edge.getRelation().toLowerCase();
                    // Check if the relation indicates a prerequisite
                    if (relation.contains("prerequisite")) {
                        prerequisites.add(fromCourse);
                    }
                }
            }
        }
        return prerequisites;
    }

    public Map<String, Object> canEnrollWithDetails(String courseCode, String studentId, Map<String, Set<String>> enrollments) {
        Set<String> visited = new HashSet<>();
        List<String> missingPrereqs = new ArrayList<>();
        boolean canEnroll = hasMetPrerequisitesWithDetails(courseCode, studentId, enrollments, visited, missingPrereqs);
        Map<String, Object> result = new HashMap<>();
        result.put("canEnroll", canEnroll);
        result.put("missingPrerequisites", missingPrereqs);
        return result;
    }

    private boolean hasMetPrerequisitesWithDetails(String courseCode, String studentId, Map<String, Set<String>> enrollments,
                                        Set<String> visited, List<String> missingPrereqs) {
        Set<String> studentCourses = enrollments.getOrDefault(studentId, new HashSet<>());
        List<String> prerequisites = getPrerequisites(courseCode);

        if (visited.contains(courseCode)) {
            return true; // Prevent infinite loops
        }
        visited.add(courseCode);
        
        // If student is already enrolled in this course, they meet the requirement
        if (studentCourses.contains(courseCode)) {
            return true;
        }
        
        // If no prerequisites, student can enroll
        if (prerequisites.isEmpty()) {
            return true;
        }
        
        // Check ALL prerequisites are met
        boolean allMet = true;
        for (String prereq : prerequisites) {
            // Check if student has completed this prerequisite
            if (!studentCourses.contains(prereq)) {
                // If not completed, check if the prerequisite's prerequisites are met
                Set<String> newVisited = new HashSet<>(visited);
                boolean prereqMet = hasMetPrerequisitesWithDetails(prereq, studentId, enrollments, newVisited, missingPrereqs);
                
                // If the prerequisite itself can't be enrolled (its prereqs not met), then this course can't be enrolled
                if (!prereqMet) {
                    missingPrereqs.add(prereq + " (and its prerequisites not met)");
                    allMet = false;
                } else {
                    // The prerequisite can be enrolled, but student hasn't taken it yet
                    missingPrereqs.add(prereq);
                    allMet = false;
                }
            }
            // If student has the prerequisite, no need to check further for this prereq
        }
        return allMet;
    }
}