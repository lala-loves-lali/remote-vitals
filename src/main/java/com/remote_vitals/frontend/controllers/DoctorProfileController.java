package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the doctor profile page.
 * Extends the base profile controller with doctor-specific functionality.
 */
public class DoctorProfileController extends ProfileController {

    @FXML
    private TextArea descriptionTextArea;
    
    @FXML
    private TextField qualificationField;
    
    @FXML
    private TextField emailField;
    
    private Doctor doctor;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        super.initialize(location, resources);
//
//        // Make email field non-editable
//        emailField.setEditable(false);
//
//        // Get doctor from current user
//        if ((doctor = BaseController.getDoctorUser()) != null) {
//            loadDoctorData();
//        }
//        else {
//            showErrorAlert("Error", "Invalid User Type",
//                    "Expected Doctor user type but got different type.");
//        }
    }

    /**
     * Loads the doctor data from the current user object.
     */
    private void loadDoctorData() {
        if (doctor != null) {
            // Load basic info
            firstNameField.setText(doctor.getFirstName());
            lastNameField.setText(doctor.getLastName());
            emailField.setText(doctor.getEmail());
            phoneField.setText(doctor.getPhoneNumber());
            descriptionTextArea.setText(doctor.getDescription());
            
            // Load qualification string
            qualificationField.setText(doctor.getQualificationString());
        }
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
//        if (doctor == null) {
//            showErrorAlert("Error", "No Doctor Data",
//                    "Unable to save changes. Doctor data not found.");
//            return;
//        }
//
//        // Update doctor data
//        doctor.setFirstName(firstNameField.getText().trim());
//        doctor.setLastName(lastNameField.getText().trim());
//        doctor.setPhoneNumber(phoneField.getText().trim());
//        doctor.setDescription(descriptionTextArea.getText().trim());
//        doctor.setQualificationString(qualificationField.getText().trim());
//
//        // Save changes
//        try {
//            BaseController.getDb().updateDoctor(doctor);
//            showInfoAlert("Success", "Profile Updated",
//                    "Your profile has been updated successfully.");
//        } catch (Exception e) {
//            showErrorAlert("Error", "Update Failed",
//                    "Failed to update profile. Please try again.");
//        }
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