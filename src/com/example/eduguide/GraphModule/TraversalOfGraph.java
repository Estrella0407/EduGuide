package com.example.eduguide.GraphModule;

import java.util.*;

import com.example.eduguide.Edge;
import com.example.eduguide.Login;

public class TraversalOfGraph {
    public boolean canEnroll(GraphOperations graph, String course, String student, 
            Map<String, Set<String>> enrolledCourses) {
        
        // Initialize visited set for tracking traversed courses
        Set<String> visited = new HashSet<>();
        
        return canEnrollHelper(graph, course, student, enrolledCourses, visited);
    }

    private boolean canEnrollHelper(GraphOperations graph, String course, String student, 
            Map<String, Set<String>> enrolledCourses, Set<String> visited) {
            
        // Check if student is already enrolled in the course
        if (enrolledCourses.containsKey(student) && 
            enrolledCourses.get(student).contains(course)) {
            return true;
        }

        // Check if we've already visited this course in current traversal
        if (visited.contains(course)) {
            return false;
        }

        // Mark current course as visited
        visited.add(course);

        // Get prerequisites for the course
        List<Edge> prerequisites = graph.getEdges(course);
        if (prerequisites == null) {
            return true; // No prerequisites
        }

        // Check all prerequisites
        for (Edge edge : prerequisites) {
            if (edge.getRelation().equalsIgnoreCase("hasPrerequisite")) {
                if (!canEnrollHelper(graph, edge.getToVertex(), student, enrolledCourses, visited)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void traversalOfGraphMenu(GraphOperations graph, Login login, 
            Map<String, Set<String>> studentEnrollments) {
        Scanner scanner = new Scanner(System.in);
        boolean continueEnrolling = true;

        while (continueEnrolling) {
            System.out.println("\nCourse Enrollment Menu");
            System.out.println("=====================");
            System.out.println("1. View Available Courses");
            System.out.println("2. Enroll in a Course");
            System.out.println("3. View My Prerequisites");
            System.out.println("4. Return to Main Menu");
            
            System.out.print("\nEnter your choice: ");
            int choice = getValidInput(scanner);

            switch (choice) {
                case 1:
                    displayAvailableCourses(graph);
                    break;
                case 2:
                    handleCourseEnrollment(graph, login, studentEnrollments, scanner);
                    break;
                case 3:
                    viewPrerequisites(graph, scanner);
                    break;
                case 4:
                    continueEnrolling = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private int getValidInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return input;
    }

    private void displayAvailableCourses(GraphOperations graph) {
        System.out.println("\nAvailable Courses:");
        System.out.println("=================");
        Map<String, List<Edge>> courses = graph.getGraph();
        for (String course : courses.keySet()) {
            System.out.println("- " + course);
        }
    }

    private void handleCourseEnrollment(GraphOperations graph, Login login, 
            Map<String, Set<String>> studentEnrollments, Scanner scanner) {
        System.out.print("\nEnter course code to enroll: ");
        String course = scanner.nextLine().trim().toUpperCase();
        
        if (!graph.getGraph().containsKey(course)) {
            System.out.println("Error: Course not found!");
            return;
        }

        String studentId = login.getCurrentUserId();
        studentEnrollments.putIfAbsent(studentId, new HashSet<>());
        
        if (studentEnrollments.get(studentId).contains(course)) {
            System.out.println("You are already enrolled in this course!");
            return;
        }

        if (canEnroll(graph, course, studentId, studentEnrollments)) {
            studentEnrollments.get(studentId).add(course);
            System.out.println("Successfully enrolled in " + course);
        } else {
            System.out.println("Cannot enroll in " + course + ". Prerequisites not met.");
        }
    }

    private void viewPrerequisites(GraphOperations graph, Scanner scanner) {
        System.out.print("\nEnter course code to view prerequisites: ");
        String course = scanner.nextLine().trim().toUpperCase();
        
        if (!graph.getGraph().containsKey(course)) {
            System.out.println("Error: Course not found!");
            return;
        }

        List<Edge> prerequisites = graph.getEdges(course);
        if (prerequisites == null || prerequisites.isEmpty()) {
            System.out.println("This course has no prerequisites.");
            return;
        }

        System.out.println("\nPrerequisites for " + course + ":");
        for (Edge edge : prerequisites) {
            if (edge.getRelation().equalsIgnoreCase("hasPrerequisite")) {
                System.out.println("- " + edge.getToVertex());
            }
        }
    }
}
