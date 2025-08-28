package com.example.eduguide;

import java.util.*;

public class Login {
    private Map<String, User> users;
    private User currentUser;

    public Login() {
        users = new HashMap<>();
        // Add some sample users for testing
        initializeUsers();
    }

    private void initializeUsers() {
        // Add sample staff
        users.put("STAFF001", new User("STAFF001", "password123", true));
        users.put("STAFF002", new User("STAFF002", "password123", true));
        
        // Add sample students
        users.put("STU001", new User("STU001", "password123", false));
        users.put("STU002", new User("STU002", "password123", false));
    }

    public boolean authenticate(String userId, String password) {
        if (!users.containsKey(userId)) {
            System.out.println("User ID not found.");
            return false;
        }

        User user = users.get(userId);
        if (user.validatePassword(password)) {
            currentUser = user;
            return true;
        } else {
            System.out.println("Invalid password.");
            return false;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isUserStaff() {
        return currentUser != null && currentUser.isStaff();
    }

    public boolean isUserStudent() {
        return currentUser != null && !currentUser.isStaff();
    }

    public String getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : null;
    }

    public void logout() {
        currentUser = null;
    }
}
