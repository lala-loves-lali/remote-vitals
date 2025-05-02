package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

/**
 * Controller for the signup page.
 * Handles user registration and navigation.
 */
public class SignupController extends BaseController {

    @FXML
    private TextField name_input;
    
    @FXML
    private TextField email_input;
    
    @FXML
    private PasswordField password_input;
    
    @FXML
    private PasswordField confirm_password_input;
    
    @FXML
    private ChoiceBox<String> user_type_dropdown;
    
    @FXML
    private Button signup_button;
    
    @FXML
    private Button back_button;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize user type dropdown if exists
        if (user_type_dropdown != null) {
            user_type_dropdown.getItems().addAll("Patient", "Doctor");
            user_type_dropdown.setValue("Patient");
        }
    }
    
    /**
     * Handles the signup button click event.
     * Registers the user and navigates to the appropriate dashboard.
     * 
     * @param event The action event
     */
    @FXML
    private void handleSignup(ActionEvent event) {
        // Get input values
        String name = name_input != null ? name_input.getText().trim() : "";
        String email = email_input.getText().trim();
        String password = password_input.getText().trim();
        String confirmPassword = confirm_password_input != null ? 
                confirm_password_input.getText().trim() : "";
        String userType = user_type_dropdown != null ? 
                user_type_dropdown.getValue() : "Patient";
        
        // Input validation
        if (email.isEmpty() || password.isEmpty()) {
            showErrorAlert("Signup Error", "Missing Information", 
                    "Please fill in all required fields.");
            return;
        }
        
        if (confirm_password_input != null && !password.equals(confirmPassword)) {
            showErrorAlert("Signup Error", "Password Mismatch", 
                    "Passwords do not match. Please try again.");
            return;
        }
        
        // For demonstration purposes, we'll use a simple role-based navigation
        // In a real application, this would involve a service call to register the user
        
        showInfoAlert("Registration Success", "Account Created", 
                "Your account has been created successfully. Please login.");
        
        // Navigate to login page
        navigateTo(event, ScreenPaths.LOGIN_PAGE, ScreenPaths.TITLE_LOGIN);
    }
    
    /**
     * Handles the back button click event.
     * Navigates back to the login page.
     * 
     * @param event The action event
     */
    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.LOGIN_PAGE, ScreenPaths.TITLE_LOGIN);
    }
} 