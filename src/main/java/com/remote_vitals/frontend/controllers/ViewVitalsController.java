package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller for the view vitals screen.
 * Provides navigation to upload and graph screens.
 */
public class ViewVitalsController extends BaseController {
    
    @FXML
    private Button uploadVitalsButton;
    
    @FXML
    private Button viewGraphsButton;
    
    @FXML
    private Label id_label;
    
    @FXML
    private Label name_label;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Set mock patient info for demonstration
        id_label.setText("P12345");
        name_label.setText("John Doe");
    }
    
    /**
     * Handles the upload vitals button click event.
     * Navigates to the upload vitals screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleUploadVitals(ActionEvent event) {
        navigateTo(event, ScreenPaths.UPLOAD_VITALS_DATA, ScreenPaths.TITLE_UPLOAD_VITALS);
    }
    
    /**
     * Handles the view graphs button click event.
     * Navigates to the vitals graph screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleViewGraphs(ActionEvent event) {
        navigateTo(event, ScreenPaths.VITALS_GRAPH, ScreenPaths.TITLE_VITALS_GRAPH);
    }
    
    /**
     * Handles the dashboard button click event.
     * Navigates to the patient dashboard screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleDashboard(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
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
     * Handles the appointments button click event.
     * Navigates to the schedule appointment screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleAppointments(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_APPOINTMENTS, ScreenPaths.TITLE_PATIENT_APPOINTMENTS);
    }
    
    /**
     * Handles the logout button click event.
     * Navigates back to the login screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        navigateTo(event, ScreenPaths.LOGIN_PAGE, ScreenPaths.TITLE_LOGIN);
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