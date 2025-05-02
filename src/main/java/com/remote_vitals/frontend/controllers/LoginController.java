package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for the login page.
 * Handles user authentication and navigation to the appropriate dashboard.
 */
public class LoginController extends BaseController {

    @FXML
    private TextField email_input;
    
    @FXML
    private PasswordField password_input;
    
    @FXML
    private Button login_button;
    
    @FXML
    private Button signup_button;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialization code goes here
    }
    
    /**
     * Handles the login button click event.
     * Authenticates the user and navigates to the appropriate dashboard.
     * 
     * @param event The action event
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String email = email_input.getText().trim();
        String password = password_input.getText().trim();
        
        // Input validation
        if (email.isEmpty() || password.isEmpty()) {
            showErrorAlert("Login Error", "Missing Information", 
                    "Please enter both email and password.");
            return;
        }
        
        // For demonstration purposes, we'll use a simple role-based navigation
        // In a real application, this would involve a service call to authenticate the user
        
        // For testing - will be replaced with actual authentication
        if (email.contains("admin")) {
            navigateTo(event, ScreenPaths.ADMIN_DASHBOARD, ScreenPaths.TITLE_ADMIN_DASHBOARD);
        } else if (email.contains("doctor")) {
            navigateTo(event, ScreenPaths.DOCTOR_DASHBOARD, ScreenPaths.TITLE_DOCTOR_DASHBOARD);
        } else {
            navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
        }
    }
    
    /**
     * Handles the sign up button click event.
     * Navigates to the sign up page.
     * 
     * @param event The action event
     */
    @FXML
    private void handleSignup(ActionEvent event) {
        navigateTo(event, ScreenPaths.SIGNUP_PAGE, ScreenPaths.TITLE_SIGNUP);
    }
} 