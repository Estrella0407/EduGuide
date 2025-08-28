package com.example.eduguide;

import java.util.*;

import com.example.eduguide.GraphModule.GraphDisplay;
import com.example.eduguide.GraphModule.GraphOperations;
import com.example.eduguide.GraphModule.TraversalOfGraph;

public class App {
    private static Login login;
    private static GraphOperations graph;
    private static GraphDisplay graphDisplay;
    private static Map<String, Set<String>> studentEnrollments;
        
    public static void main(String[] args) throws Exception {
        login = new Login();
        graph = new GraphOperations();
        graphDisplay = new GraphDisplay();
        studentEnrollments = new HashMap<>();
        
        Scanner scanner = new Scanner(System.in);
        
        // Handle login
        while (!handleLogin(scanner)) {
            System.out.println("Please try again.");
        }

        int menuOption;
        boolean running = true;

        while (running) {
            displayMenu();
            menuOption = getValidInput(scanner);

            if (login.isUserStaff()) {
                running = handleStaffOption(menuOption);
            } else {
                running = handleStudentOption(menuOption);
            }
        }
        
        System.out.println("Thank you for using University Course System!");
        scanner.close();
    }

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
        scanner.nextLine(); // Consume newline
        return input;
    }

    private static boolean handleStaffOption(int option) {
        switch (option) {
            case 1:
                CLEAR_SCREEN();
                graph.createGraphMenu();
                return true;
            case 2:
                CLEAR_SCREEN();
                // TODO: Implement course search
                return true;
            case 3:
                CLEAR_SCREEN();
                graphDisplay.universityCourseNetwork(graph);
                return true;
            case 4:
                return false; // Exit
            default:
                System.out.println("Invalid option. Please try again.");
                return true;
        }
    }

    private static boolean handleStudentOption(int option) {
        switch (option) {
            case 1:
                CLEAR_SCREEN();
                TraversalOfGraph traversal = new TraversalOfGraph();
                traversal.traversalOfGraphMenu(graph, login, studentEnrollments);
                return true;
            case 2:
                CLEAR_SCREEN();
                // TODO: Implement course search
                return true;
            case 3:
                CLEAR_SCREEN();
                graphDisplay.universityCourseNetwork(graph);
                return true;
            case 4:
                CLEAR_SCREEN();
                // TODO: Implement view enrolled courses
                return true;
            case 5:
                CLEAR_SCREEN();
                // TODO: Implement course recommendations
                return true;
            case 6:
                return false; // Exit
            default:
                System.out.println("Invalid option. Please try again.");
                return true;
        }
    }

    private static boolean handleLogin(Scanner scanner) {
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
        System.out.println("4. Exit");
    }

    static void studentMenu() {
        System.out.println("Student ID: " + login.getCurrentUserId());
        System.out.println("\n1. Enroll in a Course");
        System.out.println("2. Search for a Course"); 
        System.out.println("3. View the University Course Network");
        System.out.println("4. View Current Enrolled Courses");
        System.out.println("5. Recommend Future Courses");
        System.out.println("6. Exit");
    }
}