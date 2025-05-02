package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the dashboard selector screen.
 * Provides quick access to different dashboards and screens for testing purposes.
 */
public class DashboardSelectorController extends BaseController {

    @FXML
    private Button patientDashboardButton;
    
    @FXML
    private Button doctorDashboardButton;
    
    @FXML
    private Button adminDashboardButton;
    
    @FXML
    private Button loginButton;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // No initialization needed for this simple selector
    }
    
    // Dashboard navigation methods
    
    /**
     * Handles the patient dashboard button click event.
     * Navigates to the patient dashboard.
     * 
     * @param event The action event
     */
    @FXML
    private void handlePatientDashboard(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
    }
    
    /**
     * Handles the doctor dashboard button click event.
     * Navigates to the doctor dashboard.
     * 
     * @param event The action event
     */
    @FXML
    private void handleDoctorDashboard(ActionEvent event) {
        navigateTo(event, ScreenPaths.DOCTOR_DASHBOARD, ScreenPaths.TITLE_DOCTOR_DASHBOARD);
    }
    
    /**
     * Handles the admin dashboard button click event.
     * Navigates to the admin dashboard.
     * 
     * @param event The action event
     */
    @FXML
    private void handleAdminDashboard(ActionEvent event) {
        navigateTo(event, ScreenPaths.ADMIN_DASHBOARD, ScreenPaths.TITLE_ADMIN_DASHBOARD);
    }
    
    // Profile navigation methods
    
    /**
     * Handles the patient profile button click event.
     * Navigates to the patient profile screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handlePatientProfile(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_PROFILE, ScreenPaths.TITLE_PATIENT_PROFILE);
    }
    
    /**
     * Handles the doctor profile button click event.
     * Navigates to the doctor profile screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleDoctorProfile(ActionEvent event) {
        navigateTo(event, ScreenPaths.DOCTOR_PROFILE, ScreenPaths.TITLE_DOCTOR_PROFILE);
    }
    
    /**
     * Handles the admin profile button click event.
     * Navigates to the admin profile screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleAdminProfile(ActionEvent event) {
        navigateTo(event, ScreenPaths.ADMIN_PROFILE, ScreenPaths.TITLE_ADMIN_PROFILE);
    }
    
    // Feature navigation methods
    
    /**
     * Handles the vitals graph button click event.
     * Navigates to the vitals graph screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleVitalsGraph(ActionEvent event) {
        navigateTo(event, ScreenPaths.VITALS_GRAPH, ScreenPaths.TITLE_VITALS_GRAPH);
    }
    
    /**
     * Handles the schedule appointment button click event.
     * Navigates to the schedule appointment screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleScheduleAppointment(ActionEvent event) {
        navigateTo(event, ScreenPaths.SCHEDULE_APPOINTMENT, ScreenPaths.TITLE_SCHEDULE_APPOINTMENT);
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
    
    // System navigation methods
    
    /**
     * Handles the login button click event.
     * Navigates to the login screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        navigateTo(event, ScreenPaths.LOGIN_PAGE, ScreenPaths.TITLE_LOGIN);
    }
    
    /**
     * Handles the signup button click event.
     * Navigates to the signup screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleSignup(ActionEvent event) {
        navigateTo(event, ScreenPaths.SIGNUP_PAGE, ScreenPaths.TITLE_SIGNUP);
    }
    
    /**
     * Handles the exit button click event.
     * Closes the application.
     * 
     * @param event The action event
     */
    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }
} 