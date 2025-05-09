package com.remote_vitals.frontend.controllers;


import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Qualification;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

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
    
    @FXML
    private TextField emailField;
    
    @FXML
    private Button addQualificationButton;
    
    @FXML
    private Button removeQualificationButton;
    
    private Doctor doctor;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    @Override
    protected void initialize() {
        userType = "Doctor";
        super.initialize();
        
        // Make email field non-editable
        emailField.setEditable(false);
        
        // Get doctor from current user
        if ( (doctor = BaseController.getDoctorUser()) != null) {
            loadDoctorData();
        }
        else {
            showErrorAlert("Error", "Invalid User Type",
                    "Expected Doctor user type but got different type.");
        }
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

            // Load qualifications
            qualificationsList.getItems().clear();
            if (doctor.getQualifications() != null) {
                for (Qualification qual : doctor.getQualifications()) {
                    qualificationsList.getItems().add(qual.getLabel());
                }
            }
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
        if (doctor == null) {
            showErrorAlert("Error", "No Doctor Data",
                    "Unable to save changes. Doctor data not found.");
            return;
        }

        // Update doctor data
        doctor.setFirstName(firstNameField.getText().trim());
        doctor.setLastName(lastNameField.getText().trim());
        doctor.setPhoneNumber(phoneField.getText().trim());
        doctor.setDescription(descriptionTextArea.getText().trim());

        // Save changes
        try {
           // BaseController.getDb().updateUser(doctor);
            showInfoAlert("Success", "Profile Updated",
                    "Your profile has been updated successfully.");
        } catch (Exception e) {
            showErrorAlert("Error", "Update Failed",
                    "Failed to update profile. Please try again.");
        }
    }

    /**
     * Handles adding a new qualification.
     *
     * @param event The action event
     */
    @FXML
    private void handleAddQualification(ActionEvent event) {
        String newQualification = newQualificationField.getText().trim();

        if (newQualification.isEmpty()) {
            showErrorAlert("Validation Error", "Empty Qualification",
                    "Please enter a qualification before adding.");
            return;
        }

        try {
            Qualification qualification = new Qualification();
            qualification.setLabel(newQualification);

            int result = BaseController.getDb().addQualificationTo(doctor, qualification);
            if (result == 0) {
                qualificationsList.getItems().add(newQualification);
                newQualificationField.clear();
                showInfoAlert("Success", "Qualification Added",
                        "New qualification has been added successfully.");
            } else {
                showErrorAlert("Error", "Add Failed",
                        "Failed to add qualification. Please try again.");
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Add Failed",
                    "Failed to add qualification. Please try again.");
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

        if (selectedIndex < 0) {
            showErrorAlert("Selection Error", "No Qualification Selected",
                    "Please select a qualification to remove.");
            return;
        }

        try {
            // Get the qualification to remove
            String qualificationLabel = qualificationsList.getItems().get(selectedIndex);
            Qualification qualificationToRemove = doctor.getQualifications().stream()
                .filter(q -> q.getLabel().equals(qualificationLabel))
                .findFirst()
                .orElse(null);

            if (qualificationToRemove != null) {
                // Remove from database
               // BaseController.getDb().removeQualificationFrom(doctor, qualificationToRemove);
                // Remove from list
                qualificationsList.getItems().remove(selectedIndex);
                showInfoAlert("Success", "Qualification Removed", 
                        "Qualification has been removed successfully.");
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Remove Failed", 
                    "Failed to remove qualification. Please try again.");
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