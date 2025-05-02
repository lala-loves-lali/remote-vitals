package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.chart.LineChart;

/**
 * Controller for the patient dashboard screen.
 * Handles patient-specific functionality and navigation.
 */
public class PatientDashboardController extends BaseController {

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Button uploadVitalsButton;
    
    @FXML
    private Button scheduleAppointmentButton;
    
    @FXML
    private Button viewVitalsButton;
    
    @FXML
    private Button profileButton;
    
    @FXML
    private TableView<?> appointmentsTable;
    
    @FXML
    private LineChart<?, ?> vitalsChart;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Set welcome message
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, Patient");
        }
        
        // Initialize tables, charts, and other UI elements
        initializeAppointmentsTable();
        initializeVitalsChart();
    }
    
    /**
     * Initializes the appointments table with mock data.
     * In a real application, this would fetch data from a service.
     */
    private void initializeAppointmentsTable() {
        // Implementation depends on the specific TableView structure
        // This would typically involve setting up columns and loading data
    }
    
    /**
     * Initializes the vitals chart with mock data.
     * In a real application, this would fetch data from a service.
     */
    private void initializeVitalsChart() {
        // Implementation depends on the specific LineChart structure
        // This would typically involve setting up axes and loading data
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
     * Handles the view vitals button click event.
     * Navigates to the vitals graph screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleViewVitals(ActionEvent event) {
        navigateTo(event, ScreenPaths.VITALS_GRAPH, ScreenPaths.TITLE_VITALS_GRAPH);
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