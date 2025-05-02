package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the view vitals screen.
 * Displays patient vitals data and provides navigation to upload and graph screens.
 */
public class ViewVitalsController extends BaseController {

    @FXML
    private Label heartRateLabel;
    
    @FXML
    private Label oxygenLabel;
    
    @FXML
    private Label temperatureLabel;
    
    @FXML
    private Label bloodPressureLabel;
    
    @FXML
    private Label lastUpdatedLabel;
    
    @FXML
    private Label noDataLabel;
    
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
        
        // Set mock vitals data for demonstration
        heartRateLabel.setText("72");
        oxygenLabel.setText("98");
        temperatureLabel.setText("37.1");
        bloodPressureLabel.setText("120/80");
        
        // Mock last updated timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        lastUpdatedLabel.setText("Last updated: " + now.format(formatter));
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
        navigateTo(event, ScreenPaths.SCHEDULE_APPOINTMENT, ScreenPaths.TITLE_SCHEDULE_APPOINTMENT);
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
} 