package com.example.eduguide;

import java.util.*;
import com.example.eduguide.GraphModule.GraphDisplay;
import com.example.eduguide.GraphModule.GraphOperations;
import com.example.eduguide.GraphModule.SearchCourse;
import com.example.eduguide.GraphModule.EnrollInCourse;

public class App {
    private static Login login;
    private static GraphOperations graph;
    private static GraphDisplay graphDisplay;
    private static SearchCourse search;
    private static Map<String, Set<String>> studentEnrollments;
    private static EnrollInCourse enroll;
        
    public static void main(String[] args) throws Exception {
        login = new Login();
        graph = new GraphOperations();
        graphDisplay = new GraphDisplay();
        studentEnrollments = new HashMap<>();
        enroll = new EnrollInCourse(graph, login, studentEnrollments);
        search = new SearchCourse();
        
        Scanner scanner = new Scanner(System.in);
        
        while (!handleLogin(scanner)) {
            System.out.println("Please try again.");
        }

        int menuOption;
        boolean running = true;

        while (running) {
            displayMenu();
            menuOption = getValidInput(scanner);

            if (login.isUserStaff()) {
                running = handleStaffOption(menuOption, scanner);
            } else {
                running = handleStudentOption(menuOption, scanner);
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
                return true;
            case 3:
                CLEAR_SCREEN();
                graphDisplay.universityCourseNetwork(graph);
                pauseForUser(scanner);
                return true;
            case 4:
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
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
                return true;
            case 3:
                CLEAR_SCREEN();
                graphDisplay.universityCourseNetwork(graph);
                pauseForUser(scanner);
                return true;
            case 4:
                CLEAR_SCREEN();
                viewEnrolledCourses();
                pauseForUser(scanner);
                return true;
            case 5:
                CLEAR_SCREEN();
                viewCourseRecommendations();
                pauseForUser(scanner);
                return true;
            case 6:
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
                return true;
        }
    }

    private static void viewEnrolledCourses() {
        String studentId = login.getCurrentUserId();
        Set<String> enrolledCourses = studentEnrollments.get(studentId);
        
        System.out.println("\n=== YOUR ENROLLED COURSES ===");
        System.out.println("Student ID: " + studentId);
        
        if (enrolledCourses == null || enrolledCourses.isEmpty()) {
            System.out.println("You are not enrolled in any courses yet.");
        } else {
            List<String> sortedCourses = new ArrayList<>(enrolledCourses);
            Collections.sort(sortedCourses);
            
            for (int i = 0; i < sortedCourses.size(); i++) {
                System.out.println((i + 1) + ". " + sortedCourses.get(i));
            }
            System.out.println("\nTotal enrolled courses: " + enrolledCourses.size());
        }
    }

    private static void viewCourseRecommendations() {
        String studentId = login.getCurrentUserId();
        graphDisplay.recommendCourses(graph, studentId, studentEnrollments);
    }

    private static void pauseForUser(Scanner scanner) {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
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