package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller for the dashboard selector screen.
 * Provides quick access to different dashboards for testing purposes.
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
     * Handles the vitals graph button click event.
     * Navigates to the vitals graph screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleVitalsGraph(ActionEvent event) {
        navigateTo(event, ScreenPaths.VITALS_GRAPH, ScreenPaths.TITLE_VITALS_GRAPH);
    }
} 