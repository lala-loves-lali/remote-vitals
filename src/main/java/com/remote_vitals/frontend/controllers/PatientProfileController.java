package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.services.UserService;
import com.remote_vitals.backend.user.dtos.PatientUpdateDto;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.entities.User;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for the patient profile page.
 * Extends the base profile controller with patient-specific functionality.
 */
public class PatientProfileController extends ProfileController {

    @FXML
    private Label userNameLabel;
    @FXML
    private Label userIdLabel;
    @FXML
    private Label userTypeLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;

    // Editable fields
    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField medicalHistoryField;
    @FXML
    private TextField nextOfKinEmailField;

    private Patient patient;

    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    @Override
    protected void initialize() {
        patient = getPatientUser();
        if (patient == null) {
            showErrorAlert("Error", "Invalid User Type",
                    "Expected Patient user type but got different type.");
            return;
        }
        
        // Update labels with current user data
        String fullName = patient.getFirstName() + " " + patient.getLastName();
        userNameLabel.setText(fullName);
        nameLabel.setText(fullName);
        userTypeLabel.setText("Patient");
        userIdLabel.setText("ID: " + patient.getId());
        emailLabel.setText(patient.getEmail());

        // Load editable fields
        phoneField.setText(patient.getPhoneNumber());
        medicalHistoryField.setText(patient.getMedicalHistory());
        nextOfKinEmailField.setText(patient.getNextOfKinEmail());

        super.initialize();
    }

    /**
     * Loads the patient data from the current user object.
     */
    @FXML
    @Override
    protected void loadUserData() {
        if (patient == null) {
            patient = getPatientUser();
            if (patient == null) {
                showErrorAlert("Error", "No Patient Data",
                        "Unable to load patient data. Please try logging in again.");
                return;
            }
        }

        // Update labels
        String fullName = patient.getFirstName() + " " + patient.getLastName();
        userNameLabel.setText(fullName);
        nameLabel.setText(fullName);
        userTypeLabel.setText("Patient");
        userIdLabel.setText("ID: " + patient.getId());
        emailLabel.setText(patient.getEmail());

        // Load editable fields
        phoneField.setText(patient.getPhoneNumber());
        medicalHistoryField.setText(patient.getMedicalHistory());
        nextOfKinEmailField.setText(patient.getNextOfKinEmail());
    }

    /**
     * Handles the Load My Data button click event.
     * Reloads patient data from the database.
     */
    @FXML
    private void handleLoadData(ActionEvent event) {
        loadUserData();
        enterAlert("Success", "Data Loaded", "Your profile data has been reloaded successfully.");
    }

    /**
     * Handles the save button click event.
     * Saves the patient profile changes.
     */
    @FXML
    @Override
    protected void handleSave(ActionEvent event) {
        if (patient == null) {
            patient = getPatientUser();
            if (patient == null) {
                showErrorAlert("Error", "No Patient Data",
                        "Unable to save changes. Patient data not found.");
                return;
            }
        }

        // Validate required fields
        if (phoneField.getText().isEmpty()) {
            showErrorAlert("Validation Error", "Missing Information",
                    "Please fill in all required fields.");
            return;
        }

        // Create update DTO with editable fields
        PatientUpdateDto updateDto = new PatientUpdateDto(
            phoneField.getText().trim(),
            patient.getPnVisibility(), // Keep existing phone visibility
            patient.getEmail(), // Keep existing email
            patient.getEVisibility(), // Keep existing email visibility
            passwordField.getText().isEmpty() ? null : passwordField.getText(), // Only update if changed
            medicalHistoryField.getText().trim(),
            nextOfKinEmailField.getText().trim()
        );

        // Save changes to database
        try {
            getContext().getBean(UserService.class).updateUser(updateDto);
            enterAlert("Success", "Profile Updated",
                    "Your profile has been updated successfully.");
        } catch (Exception e) {
            showErrorAlert("Error", "Update Failed",
                    "An error occurred while updating your profile. Please try again.");
        }
    }

    /**
     * Handles the cancel button click event.
     * Discards changes and reloads patient data.
     */
    @FXML
    @Override
    protected void handleCancel(ActionEvent event) {
        loadUserData();
    }

    /**
     * Handles the back button click event.
     * Navigates back to the patient dashboard.
     */
    @FXML
    @Override
    protected void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
    }

    private void enterAlert(String title, String header, String content) {
        showInfoAlert(title, header, content);
        loadUserData();
    }
} 