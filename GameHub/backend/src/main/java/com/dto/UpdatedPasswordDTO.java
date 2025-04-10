package com.dto;

public class UpdatedPasswordDTO{
    private String currentPassword;
    private String newPassword;

    // Getters and Setters
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setcurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}