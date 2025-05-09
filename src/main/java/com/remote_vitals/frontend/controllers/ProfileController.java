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
        // Do not call loadUserData() here as it will be called by subclasses
    }
    
    /**
     * Loads the user data from the backend.
     * This method should be overridden by subclasses to load specific user type data.
     */
    protected void loadUserData() {
        // This method should be overridden by subclasses
        // Do not provide default implementation to force subclasses to implement it
    }
    
    /**
     * Handles the save button click event.
     * Saves the user profile changes.
     * This method should be overridden by subclasses to implement specific save logic.
     * 
     * @param event The action event
     */
    @FXML
    protected void handleSave(ActionEvent event) {
        // This method should be overridden by subclasses
        showErrorAlert("Error", "Not Implemented", 
                "Save functionality must be implemented by the specific profile controller.");
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
     * This method should be overridden by subclasses to navigate to the correct dashboard.
     * 
     * @param event The action event
     */
    @FXML
    protected void handleBack(ActionEvent event) {
        // This should be overridden by subclasses to navigate to the appropriate dashboard
    }
} 