package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/**
 * Controller for the patient profile page.
 * Extends the base profile controller with patient-specific functionality.
 */
public class PatientProfileController extends ProfileController {

    @FXML
    private TextField bloodGroupField;
    
    @FXML
    private DatePicker dateOfBirthPicker;
    
    @FXML
    private TextField descriptionField;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    @Override
    protected void initialize() {
        userType = "Patient";
        super.initialize();
    }
    
    /**
     * Loads the patient data from the backend.
     * Overrides the base method to include patient-specific fields.
     */
    @Override
    protected void loadUserData() {
        super.loadUserData();
        
        // Load patient-specific data
        bloodGroupField.setText("O+");
        dateOfBirthPicker.setValue(LocalDate.of(1990, 1, 1));
        descriptionField.setText("Patient with history of hypertension.");
    }
    
    /**
     * Handles the save button click event.
     * Saves the patient profile changes.
     * 
     * @param event The action event
     */
    @FXML
    @Override
    protected void handleSave(ActionEvent event) {
        // Validate patient-specific fields
        if (bloodGroupField.getText().isEmpty() || dateOfBirthPicker.getValue() == null) {
            showErrorAlert("Validation Error", "Missing Information", 
                    "Please fill in all required fields including blood group and date of birth.");
            return;
        }
        
        // Call the parent save method
        super.handleSave(event);
    }
    
    /**
     * Handles the back button click event.
     * Navigates back to the patient dashboard.
     * 
     * @param event The action event
     */
    @FXML
    @Override
    protected void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
    }
} 