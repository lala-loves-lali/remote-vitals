package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller for the doctor profile page.
 * Extends the base profile controller with doctor-specific functionality.
 */
public class DoctorProfileController extends ProfileController {

    @FXML
    private TextArea descriptionTextArea;
    
    @FXML
    private ListView<String> qualificationsList;
    
    @FXML
    private TextField newQualificationField;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    @Override
    protected void initialize() {
        userType = "Doctor";
        super.initialize();
    }
    
    /**
     * Loads the doctor data from the backend.
     * Overrides the base method to include doctor-specific fields.
     */
    @Override
    protected void loadUserData() {
        super.loadUserData();
        
        // Load doctor-specific data
        descriptionTextArea.setText("Cardiologist with 10 years of experience specializing in cardiovascular health.");
        
        // Load qualifications
        qualificationsList.getItems().clear();
        qualificationsList.getItems().addAll(
            "MD in Cardiology, 2010",
            "Board Certified by American Board of Internal Medicine",
            "Fellowship in Cardiovascular Disease, 2013"
        );
    }
    
    /**
     * Handles the save button click event.
     * Saves the doctor profile changes.
     * 
     * @param event The action event
     */
    @FXML
    @Override
    protected void handleSave(ActionEvent event) {
        // Validate doctor-specific fields
        if (descriptionTextArea.getText().isEmpty()) {
            showErrorAlert("Validation Error", "Missing Information", 
                    "Please provide a description of your medical practice.");
            return;
        }
        
        // Call the parent save method
        super.handleSave(event);
    }
    
    /**
     * Handles adding a new qualification.
     * 
     * @param event The action event
     */
    @FXML
    private void handleAddQualification(ActionEvent event) {
        String newQualification = newQualificationField.getText().trim();
        
        if (!newQualification.isEmpty()) {
            qualificationsList.getItems().add(newQualification);
            newQualificationField.clear();
        } else {
            showErrorAlert("Validation Error", "Empty Qualification", 
                    "Please enter a qualification before adding.");
        }
    }
    
    /**
     * Handles removing a qualification.
     * 
     * @param event The action event
     */
    @FXML
    private void handleRemoveQualification(ActionEvent event) {
        int selectedIndex = qualificationsList.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex >= 0) {
            qualificationsList.getItems().remove(selectedIndex);
        } else {
            showErrorAlert("Selection Error", "No Qualification Selected", 
                    "Please select a qualification to remove.");
        }
    }
    
    /**
     * Handles the back button click event.
     * Navigates back to the doctor dashboard.
     * 
     * @param event The action event
     */
    @FXML
    @Override
    protected void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.DOCTOR_DASHBOARD, ScreenPaths.TITLE_DOCTOR_DASHBOARD);
    }
} 