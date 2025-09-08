package com.example.eduguide.GraphModule;

import java.io.*;
import java.util.*;

public class EnrollmentPersistence {
    private static final String ENROLLMENT_FILE = "student_enrollments.txt";
    
    /**
     * Save student enrollments to file
     * Format: studentId|course1,course2,course3
     */

    public static void saveEnrollments(Map<String, Set<String>> studentEnrollments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ENROLLMENT_FILE))) {
            for (Map.Entry<String, Set<String>> entry : studentEnrollments.entrySet()) {
                String studentId = entry.getKey();
                Set<String> courses = entry.getValue();
                
                if (!courses.isEmpty()) {
                    writer.write(studentId + "|" + String.join(",", courses));
                    writer.newLine();
                }
            }
            System.out.println("Enrollments saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving enrollments: " + e.getMessage());
        }
    }
    
    /**
     * Load student enrollments from file
     */

    public static Map<String, Set<String>> loadEnrollments() {
        Map<String, Set<String>> studentEnrollments = new HashMap<>();
        
        File file = new File(ENROLLMENT_FILE);
        if (!file.exists()) {
            System.out.println("No existing enrollment file found. Starting fresh.");
            return studentEnrollments;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int loadedCount = 0;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    String studentId = parts[0];
                    String[] courses = parts[1].split(",");
                    
                    Set<String> courseSet = new HashSet<>();
                    for (String course : courses) {
                        courseSet.add(course.trim());
                    }
                    
                    studentEnrollments.put(studentId, courseSet);
                    loadedCount++;
                }
            }
            
            System.out.println("Loaded " + loadedCount + " student enrollment records.");
        } catch (IOException e) {
            System.err.println("Error loading enrollments: " + e.getMessage());
        }
        
        return studentEnrollments;
    }
}