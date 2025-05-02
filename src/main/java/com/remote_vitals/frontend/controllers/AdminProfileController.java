package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

/**
 * Controller for the admin profile page.
 * Extends the base profile controller with admin-specific functionality.
 */
public class AdminProfileController extends ProfileController {

    @FXML
    private PasswordField currentPasswordField;
    
    @FXML
    private PasswordField newPasswordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    @Override
    protected void initialize() {
        userType = "Admin";
        super.initialize();
    }
    
    /**
     * Handles the save button click event.
     * Saves the admin profile changes.
     * 
     * @param event The action event
     */
    @FXML
    @Override
    protected void handleSave(ActionEvent event) {
        // Check if attempting to change password
        if (!newPasswordField.getText().isEmpty() || !confirmPasswordField.getText().isEmpty()) {
            // Validate password change
            if (currentPasswordField.getText().isEmpty()) {
                showErrorAlert("Password Error", "Current Password Required", 
                        "Please enter your current password to change to a new one.");
                return;
            }
            
            if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
                showErrorAlert("Password Error", "Password Mismatch", 
                        "New password and confirmation do not match.");
                return;
            }
            
            if (newPasswordField.getText().length() < 8) {
                showErrorAlert("Password Error", "Password Too Short", 
                        "New password must be at least 8 characters long.");
                return;
            }
            
            // In a real application, verify the current password is correct
            // For demo purposes, we assume it is
            
            showInfoAlert("Password Changed", "Success", 
                    "Your password has been changed successfully.");
            
            // Clear password fields
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();
        }
        
        // Call the parent save method for other profile details
        super.handleSave(event);
    }
    
    /**
     * Handles the back button click event.
     * Navigates back to the admin dashboard.
     * 
     * @param event The action event
     */
    @FXML
    @Override
    protected void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.ADMIN_DASHBOARD, ScreenPaths.TITLE_ADMIN_DASHBOARD);
    }
} 