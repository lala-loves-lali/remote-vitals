package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.entities.User;
import com.remote_vitals.backend.user.enums.UserType;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Controller for the patient profile page.
 * Extends the base profile controller with patient-specific functionality.
 */
public class PatientProfileController extends ProfileController {


    @FXML
    private Label   userNameLabel;

    @FXML
    private Label   userIdLabel;

    @FXML
    private Label   userTypeLabel;


    @FXML
    private TextField bloodGroupField;

    @FXML
    private DatePicker dateOfBirthPicker;

    @FXML
    private TextField descriptionField;

    private Patient patient;

    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    @Override
    protected void initialize() {
        patient = getPatientUser();
        // Update labels with current user data
        String fullName = patient.getFirstName() + " " + patient.getLastName();
        System.out.println("PatientProfileController: Setting full name to: " + fullName);
        userNameLabel.setText(fullName);
        userTypeLabel.setText(userType);
        userIdLabel.setText("ID: " + patient.getId());

        // Load basic info into text fields
        firstNameField.setText(patient.getFirstName());
        lastNameField.setText(patient.getLastName());
        emailField.setText(patient.getEmail());
        phoneField.setText(patient.getPhoneNumber());
        descriptionField.setText(patient.getDescription());
        bloodGroupField.setText(patient.getBloodGroup());

        // Convert LocalDateTime to LocalDate for DatePicker
        if (patient.getDateOfBirth() != null) {
            dateOfBirthPicker.setValue(patient.getDateOfBirth().toLocalDate());
        }

        // Load profile image if exists
        if (patient.getProfilePhoto() != null && !patient.getProfilePhoto().isEmpty()) {
            try {
                profileImageView.setImage(new Image(patient.getProfilePhoto()));
            } catch (Exception e) {
                // If image loading fails, use default image
                try {
                    profileImageView.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
                } catch (Exception ex) {
                    System.out.println("Default profile image not found");
                }
            }
        } else {
            // Use default image
            try {
                profileImageView.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
            } catch (Exception e) {
                System.out.println("Default profile image not found");
            }
        }

        System.out.println("PatientProfileController: Initializing...");
        userType = "Patient";
        super.initialize();

        // Make email field non-editable
        emailField.setEditable(false);

        // Get patient from current user
        patient = getPatientUser();
        System.out.println("PatientProfileController: Current user = " + (patient != null ? patient.getEmail() : "null"));
        System.out.println("PatientProfileController: Current user type = " + getUserType());
        
        if (patient != null) {
            System.out.println("PatientProfileController: Found patient with ID: " + patient.getId());
            loadUserData();
        } else {
            System.out.println("PatientProfileController: Current user is not a Patient or wrong user type");
            showErrorAlert("Error", "Invalid User Type",
                    "Expected Patient user type but got different type.");
        }
        handleCancel(new ActionEvent());
    }

    /**
     * Loads the patient data from the current user object.
     */
    @Override
    protected void loadUserData() {
        System.out.println("PatientProfileController: Loading user data...");
        if (patient == null) {
            // Try to get the patient from current user again
            patient = getPatientUser();
            System.out.println("PatientProfileController: Retrying to get patient from current user: " + 
                             (patient != null ? patient.getEmail() : "null"));
            System.out.println("PatientProfileController: Current user type = " + getUserType());
            
            if (patient == null) {
                System.out.println("PatientProfileController: Still not a Patient or wrong user type");
                showErrorAlert("Error", "No Patient Data",
                        "Unable to load patient data. Please try logging in again.");
                return;
            }
        }

        // Update labels with current user data
        String fullName = patient.getFirstName() + " " + patient.getLastName();
        System.out.println("PatientProfileController: Setting full name to: " + fullName);
        userNameLabel.setText(fullName);
        userTypeLabel.setText(userType);
        userIdLabel.setText("ID: " + patient.getId());

        // Load basic info into text fields
        firstNameField.setText(patient.getFirstName());
        lastNameField.setText(patient.getLastName());
        emailField.setText(patient.getEmail());
        phoneField.setText(patient.getPhoneNumber());
        descriptionField.setText(patient.getDescription());
        bloodGroupField.setText(patient.getBloodGroup());

        // Convert LocalDateTime to LocalDate for DatePicker
        if (patient.getDateOfBirth() != null) {
            dateOfBirthPicker.setValue(patient.getDateOfBirth().toLocalDate());
        }

        // Load profile image if exists
        if (patient.getProfilePhoto() != null && !patient.getProfilePhoto().isEmpty()) {
            try {
                profileImageView.setImage(new Image(patient.getProfilePhoto()));
            } catch (Exception e) {
                // If image loading fails, use default image
                try {
                    profileImageView.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
                } catch (Exception ex) {
                    System.out.println("Default profile image not found");
                }
            }
        } else {
            // Use default image
            try {
                profileImageView.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
            } catch (Exception e) {
                System.out.println("Default profile image not found");
            }
        }
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
        System.out.println("PatientProfileController: Handling save...");
        if (patient == null) {
            // Try to get the patient from current user again
            patient = getPatientUser();
            System.out.println("PatientProfileController: Retrying to get patient from current user: " + 
                             (patient != null ? patient.getEmail() : "null"));
            System.out.println("PatientProfileController: Current user type = " + getUserType());
            
            if (patient == null) {
                System.out.println("PatientProfileController: Still not a Patient or wrong user type");
                showErrorAlert("Error", "No Patient Data",
                        "Unable to save changes. Patient data not found.");
                return;
            }
        }

        // Validate required fields
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                bloodGroupField.getText().isEmpty() || dateOfBirthPicker.getValue() == null) {
            showErrorAlert("Validation Error", "Missing Information",
                    "Please fill in all required fields.");
            return;
        }

        // Validate blood group format
        String bloodGroup = bloodGroupField.getText().trim();
        if (!bloodGroup.matches("^(A|B|AB|O)[+-]$")) {
            showErrorAlert("Validation Error", "Invalid Blood Group",
                    "Blood group must be in the format A+, B-, AB+, O-, etc.");
            return;
        }

        // Update patient data
        patient.setFirstName(firstNameField.getText().trim());
        patient.setLastName(lastNameField.getText().trim());
        patient.setPhoneNumber(phoneField.getText().trim());
        patient.setDescription(descriptionField.getText().trim());
        patient.setBloodGroup(bloodGroup);
        patient.setDateOfBirth(dateOfBirthPicker.getValue().atStartOfDay());

        // Save changes to database
        try {
            int result = getDb().updatePatient(patient);
            if (result > 0) {
                showInfoAlert("Success", "Profile Updated",
                        "Your profile has been updated successfully.");
                // Update displayed name and labels
                String fullName = patient.getFirstName() + " " + patient.getLastName();
                System.out.println("PatientProfileController: Updating labels after save with name: " + fullName);
                userNameLabel.setText(fullName);
                userTypeLabel.setText(userType);
                userIdLabel.setText("ID: " + patient.getId());
                // Update current user in BaseController
                setCurrentUser(patient);
            } else {
                showErrorAlert("Error", "Update Failed",
                        "Failed to update profile. Please try again.");
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Update Failed",
                    "An error occurred while updating your profile. Please try again.");
        }
    }

    /**
     * Handles the cancel button click event.
     * Discards changes and reloads patient data.
     *
     * @param event The action event
     */
    @FXML
    @Override
    protected void handleCancel(ActionEvent event) {
        loadUserData();
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