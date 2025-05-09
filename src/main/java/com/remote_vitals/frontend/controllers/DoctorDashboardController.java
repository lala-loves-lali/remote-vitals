package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller for the doctor dashboard screen.
 * Handles doctor-specific functionality and navigation.
 */
public class DoctorDashboardController extends BaseController {

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Button profileButton;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize UI components or load data if needed
    }
    
    /**
     * Handles the monitor vitals button click event.
     * Navigates to the vitals monitoring screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleMonitorVitals(ActionEvent event) {
        // To be implemented with the vitals monitoring screen
        showInfoAlert("Feature Coming Soon", "Monitor Vitals", 
                "This feature will be available in a future update.");
    }
    
    /**
     * Handles the view patients button click event.
     * Shows the patient list.
     * 
     * @param event The action event
     */
    @FXML
    private void handleViewPatients(ActionEvent event) {
        // To be implemented with the patient list screen
        showInfoAlert("Feature Coming Soon", "View Patients", 
                "This feature will be available in a future update.");
    }
    
    /**
     * Handles the view appointments button click event.
     * Shows the appointments list.
     * 
     * @param event The action event
     */
    @FXML
    private void handleViewAppointments(ActionEvent event) {
        navigateTo(event, ScreenPaths.DOCTOR_APPOINTMENTS, ScreenPaths.TITLE_DOCTOR_APPOINTMENTS);
    }
    
    /**
     * Handles the profile button click event.
     * Navigates to the doctor profile screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleProfile(ActionEvent event) {
        navigateTo(event, ScreenPaths.DOCTOR_PROFILE, ScreenPaths.TITLE_DOCTOR_PROFILE);
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
     * Handles the dashboard selector button click event.
     * Navigates to the dashboard selector for testing.
     * 
     * @param event The action event
     */
    @FXML
    private void handleDashboardSelector(ActionEvent event) {
        navigateTo(event, ScreenPaths.DASHBOARD_SELECTOR, ScreenPaths.TITLE_DASHBOARD_SELECTOR);
    }
} 