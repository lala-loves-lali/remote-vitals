package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Base controller for profile pages.
 * Defines common profile functionality shared by all user types.
 */
public class ProfileController extends BaseController {

    @FXML
    protected Label userNameLabel;
    
    @FXML
    protected Label userTypeLabel;
    
    @FXML
    protected Label userIdLabel;
    
    @FXML
    protected ImageView profileImageView;
    
    @FXML
    protected TextField firstNameField;
    
    @FXML
    protected TextField lastNameField;
    
    @FXML
    protected TextField emailField;
    
    @FXML
    protected TextField phoneField;
    
    @FXML
    protected Button saveButton;
    
    @FXML
    protected Button cancelButton;
    
    protected String userType = "User";
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    protected void initialize() {
        // This will be overridden by subclasses to load specific user data
        loadUserData();
    }
    
    /**
     * Loads the user data from the backend.
     * This method should be overridden by subclasses to load specific user type data.
     */
    protected void loadUserData() {
        // For demonstration, use placeholder data
        userNameLabel.setText("John Doe");
        userTypeLabel.setText(userType);
        userIdLabel.setText("ID: 12345");
        
        firstNameField.setText("John");
        lastNameField.setText("Doe");
        emailField.setText("john.doe@example.com");
        phoneField.setText("123-456-7890");
        
        // Load default profile image
        try {
            profileImageView.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
        } catch (Exception e) {
            // If image doesn't exist, just continue without it
            System.out.println("Default profile image not found");
        }
    }
    
    /**
     * Handles the save button click event.
     * Saves the user profile changes.
     * 
     * @param event The action event
     */
    @FXML
    protected void handleSave(ActionEvent event) {
        // Validate inputs
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || 
                emailField.getText().isEmpty()) {
            showErrorAlert("Validation Error", "Missing Information", 
                    "Please fill in all required fields.");
            return;
        }
        
        // In a real application, this would save the data to the backend
        showInfoAlert("Profile Updated", "Success", 
                "Your profile has been updated successfully.");
        
        // Update displayed name
        userNameLabel.setText(firstNameField.getText() + " " + lastNameField.getText());
    }
    
    /**
     * Handles the cancel button click event.
     * Discards changes and reloads user data.
     * 
     * @param event The action event
     */
    @FXML
    protected void handleCancel(ActionEvent event) {
        loadUserData();
    }
    
    /**
     * Handles the back button click event.
     * Navigates back to the appropriate dashboard.
     * 
     * @param event The action event
     */
    @FXML
    protected void handleBack(ActionEvent event) {
        // This should be overridden by subclasses to navigate to the appropriate dashboard
    }
} 