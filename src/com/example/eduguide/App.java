package com.example.eduguide;

import java.util.*;
import com.example.eduguide.GraphModule.*;

public class App {
    private static Login login;
    private static GraphOperations graph;
    private static GraphDisplay graphDisplay;
    private static SearchCourse search;
    private static Map<String, Set<String>> studentEnrollments;
    private static EnrollInCourse enroll;
       
    public static void main(String[] args) throws Exception {
        // Initialize components
        login = new Login();
        graph = new GraphOperations();
        graphDisplay = new GraphDisplay();
        
        // Load existing enrollments from file
        studentEnrollments = EnrollmentPersistence.loadEnrollments();
        
        enroll = new EnrollInCourse(graph, login, studentEnrollments);
        search = new SearchCourse();
       
        Scanner scanner = new Scanner(System.in);
        
        // Add shutdown hook to save enrollments when program exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nSaving enrollments before exit...");
            EnrollmentPersistence.saveEnrollments(studentEnrollments);
        }));
       
        // Main application loop
        boolean exitSystem = false;
        while (!exitSystem) {
            // Login loop
            while (!handleLogin(scanner)) {
                System.out.println("Please try again.");
            }
            
            // Menu loop for logged-in user
            boolean returnToLogin = false;
            while (!returnToLogin) {
                displayMenu();
                int menuOption = getValidInput(scanner);
                
                if (login.isUserStaff()) {
                    if (!handleStaffOption(menuOption, scanner)) {
                        returnToLogin = true;
                    }
                } else {
                    if (!handleStudentOption(menuOption, scanner)) {
                        returnToLogin = true;
                    }
                }
            }
            
            // Ask if user wants to exit or login as different user
            System.out.print("Do you want to exit the system? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.startsWith("y")) {
                exitSystem = true;
            }
        }
       
        // Final save before closing
        EnrollmentPersistence.saveEnrollments(studentEnrollments);
        System.out.println("Thank you for using University Course System!");
        scanner.close();
    }

    // ... rest of your existing methods remain the same ...
    
    private static void CLEAR_SCREEN() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static int getValidInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    private static boolean handleStaffOption(int option, Scanner scanner) {
        switch (option) {
            case 1:
                CLEAR_SCREEN();
                graph.createGraphMenu();
                return true;
            case 2:
                CLEAR_SCREEN();
                search.searchCourse(scanner);
                pauseForUser(scanner);
                return true;
            case 3:
                CLEAR_SCREEN();
                graphDisplay.universityCourseNetwork(graph);
                pauseForUser(scanner);
                return true;
            case 4:
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
                pauseForUser(scanner);
                return true;
        }
    }

    private static boolean handleStudentOption(int option, Scanner scanner) {
        switch (option) {
            case 1:
                CLEAR_SCREEN();
                enroll.enrollInCourse(scanner);
                return true;
            case 2:
                CLEAR_SCREEN();
                search.searchCourse(scanner);
                pauseForUser(scanner);
                return true;
            case 3:
                CLEAR_SCREEN();
                graphDisplay.universityCourseNetwork(graph);
                pauseForUser(scanner);
                return true;
            case 4:
                CLEAR_SCREEN();
                new ViewCurrentCourses(login, studentEnrollments).display();
                pauseForUser(scanner);
                return true;
            case 5:
                CLEAR_SCREEN();
                new RecommendFutureCourses(login, studentEnrollments, graph).display();
                pauseForUser(scanner);
                return true;
            case 6:
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
                pauseForUser(scanner);
                return true;
        }
    }

    private static boolean handleLogin(Scanner scanner) {
        CLEAR_SCREEN();
        System.out.println("Welcome to University Course System");
        System.out.println("Please login to continue");
        System.out.println("===============================");
       
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
       
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        return login.authenticate(userId, password);
    }

    static void displayMenu() {
        CLEAR_SCREEN();
        System.out.println("\nWelcome to University Course System");
        System.out.println("=========================================");
        if (login.isUserStaff()) {
            staffMenu();
        } else if (login.isUserStudent()) {
            studentMenu();
        }
        System.out.print("\nEnter your choice: ");
    }

    static void staffMenu() {
        System.out.println("Staff ID: " + login.getCurrentUserId());
        System.out.println("\n1. Graph Operations");
        System.out.println("2. Search for a Course");
        System.out.println("3. View the University Course Network");
        System.out.println("4. Logout");
    }

    static void studentMenu() {
        System.out.println("Student ID: " + login.getCurrentUserId());
        System.out.println("\n1. Enroll in a Course");
        System.out.println("2. Search for a Course");
        System.out.println("3. View the University Course Network");
        System.out.println("4. View Current Enrolled Courses");
        System.out.println("5. Recommend Future Courses");
        System.out.println("6. Logout");
    }

    static void pauseForUser(Scanner scanner) {
        System.out.println("\nPress Enter to return to menu...");
        scanner.nextLine();
    }
}