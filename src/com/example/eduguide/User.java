package com.example.eduguide;

public class User {
    private String userId;
    private String password;
    private boolean isStaff;

    public User(String userId, String password, boolean isStaff) {
        this.userId = userId;
        this.password = password;
        this.isStaff = isStaff;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public boolean validatePassword(String inputPassword) {
        return password.equals(inputPassword);
    }
}
