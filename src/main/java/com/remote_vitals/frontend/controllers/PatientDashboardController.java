package com.remote_vitals.frontend.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.remote_vitals.backend.mailSender.EmailTemplate;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.frontend.utils.ScreenPaths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller for the patient dashboard screen.
 * Handles patient-specific functionality and navigation.
 */
public class PatientDashboardController extends BaseController implements Initializable {

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Button panic_button;
    
    @FXML
    private Label id_label;
    
    @FXML
    private Label name_label;

    private Patient currentPatient;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("Initializing PatientDashboardController...");

            // Get current patient
            currentPatient = getPatientUser();
            System.out.println("Current patient: " + (currentPatient != null ? currentPatient.getFirstName() : "null"));

            if (currentPatient == null) {
                System.out.println("No patient found.");
                showErrorAlert("Error", "Session Error", "No patient session found. Please log in again.");
                return;
            }

            // Set welcome message
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, " + currentPatient.getFirstName() + "!");
                System.out.println("Set welcome message for: " + currentPatient.getFirstName());
            }
            
            // Set patient info
            if (id_label != null) {
                id_label.setText("P" + currentPatient.getId());
            }
            
            if (name_label != null) {
                name_label.setText(currentPatient.getFirstName() + " " + currentPatient.getLastName());
            }
            
            // The panic button action is already set in FXML with onAction="#handlePanicButton"

            System.out.println("PatientDashboardController initialization complete");
        } catch (Exception e) {
            System.out.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Handle panic button click
     */
    @FXML
    public void handlePanicButton() {
        if(getPatientUser().getNextOfKinEmail() != null){
            try {
                System.out.println(getPatientUser().getNextOfKinEmail() + " is the next of kin email");

                EmailTemplate.sendEmail(getPatientUser().getNextOfKinEmail(), "Panic Button Pressed", 
                    "Panic button pressed by " + currentPatient.getFirstName() + " " + currentPatient.getLastName());
                
                // Show confirmation alert
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Emergency Alert");
                alert.setHeaderText("Emergency Assistance");
                alert.setContentText("Emergency notification has been sent to your emergency contact. Medical assistance will be contacted shortly.");
                alert.showAndWait();
            } catch (Exception e) {
                showErrorAlert("Error", "Email Error", "Error sending email to next of kin. Please try again later.");
            }
        } else {
            // No next of kin email found
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Emergency Alert");
            alert.setHeaderText("Emergency Contact Not Found");
            alert.setContentText("No emergency contact email found. Please update your profile with an emergency contact.");
            alert.showAndWait();
        }
    }

   
   
    /**
     * Handles the schedule appointment button click event.
     * Navigates to the schedule appointment screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleScheduleAppointment(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_APPOINTMENTS, ScreenPaths.TITLE_PATIENT_APPOINTMENTS);
    }
    
    /**
     * Handles the view vitals button click event.
     * Navigates to the view vitals screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleViewVitals(ActionEvent event) {
        navigateTo(event, ScreenPaths.VIEW_VITALS, ScreenPaths.TITLE_VIEW_VITALS);
    }
    
    /**
     * Handles the profile button click event.
     * Navigates to the patient profile screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleProfile(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_PROFILE, ScreenPaths.TITLE_PATIENT_PROFILE);
    }
    
    /**
     * Handles the logout button click event.
     * Logs out the current user and returns to the login screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        navigateTo(event, ScreenPaths.LOGIN_PAGE, ScreenPaths.TITLE_LOGIN);
    }
    
    /**
     * Handles the dashboard button click event.
     * Refreshes the current dashboard.
     * 
     * @param event The action event
     */
    @FXML
    private void handleDashboardSelector(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
    }
    
    /**
     * Handles the medical history button click event.
     * Navigates to the patient medical history screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleMedicalHistory(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_MEDICAL_HISTORY, ScreenPaths.TITLE_PATIENT_MEDICAL_HISTORY);
    }


} 