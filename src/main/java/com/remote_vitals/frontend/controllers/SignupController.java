package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;

/**
 * Controller for the signup page.
 * Handles user registration and navigation.
 */
public class SignupController extends BaseController {

    @FXML
    private TextField firstName_input;
    
    @FXML
    private TextField lastName_input;
    
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
    private Hyperlink loginLink;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize user type dropdown
        user_type_dropdown.getItems().addAll("Patient", "Doctor");
        user_type_dropdown.setValue("Patient");
        
        // Add listeners for instant form validation
        password_input.textProperty().addListener((obs, oldVal, newVal) -> {
            validatePassword(newVal);
        });
        
        confirm_password_input.textProperty().addListener((obs, oldVal, newVal) -> {
            validatePasswordsMatch(password_input.getText(), newVal);
        });
    }
    
    /**
     * Validates the password strength.
     * 
     * @param password The password to validate
     * @return True if the password is valid
     */
    private boolean validatePassword(String password) {
        // Basic password validation
        if (password.length() < 8) {
            // For now, we'll just check length
            // In a real application, you would check for complexity requirements
            return false;
        }
        return true;
    }
    
    /**
     * Validates that the passwords match.
     * 
     * @param password The original password
     * @param confirmPassword The confirmed password
     * @return True if the passwords match
     */
    private boolean validatePasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
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
        String firstName = firstName_input.getText().trim();
        String lastName = lastName_input.getText().trim();
        String email = email_input.getText().trim();
        String password = password_input.getText().trim();
        String confirmPassword = confirm_password_input.getText().trim();
        String userType = user_type_dropdown.getValue();
        
        // Input validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorAlert("Signup Error", "Missing Information", 
                    "Please fill in all required fields.");
            return;
        }
        
        // Email validation - simple check for "@" character
        if (!email.contains("@")) {
            showErrorAlert("Signup Error", "Invalid Email", 
                    "Please enter a valid email address.");
            return;
        }
        
        // Password validation
        if (!validatePassword(password)) {
            showErrorAlert("Signup Error", "Weak Password", 
                    "Password must be at least 8 characters long.");
            return;
        }
        
        // Confirm password match
        if (!validatePasswordsMatch(password, confirmPassword)) {
            showErrorAlert("Signup Error", "Password Mismatch", 
                    "Passwords do not match. Please try again.");
            return;
        }
        
        // For demonstration purposes, we'll show a success message
        // In a real application, this would involve a service call to register the user
        
        showInfoAlert("Registration Success", "Account Created", 
                "Your " + userType + " account has been created successfully. Please log in with your credentials.");
        
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