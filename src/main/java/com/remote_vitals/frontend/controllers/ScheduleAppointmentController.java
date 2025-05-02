package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/**
 * Controller for the schedule appointment screen.
 * Handles scheduling and managing patient appointments.
 */
public class ScheduleAppointmentController extends BaseController {

    @FXML
    private TextField link;
    
    @FXML
    private DatePicker date;
    
    @FXML
    private TextField time;
    
    @FXML
    private TextArea Desp;
    
    @FXML
    private Button scheduleButton;
    
    @FXML
    private Button backButton;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize UI components
        
        // Set default date to tomorrow
        if (date != null) {
            date.setValue(LocalDate.now().plusDays(1));
        }
    }
    
    /**
     * Handles the schedule button click event.
     * Schedules the appointment and navigates back to the patient dashboard.
     * 
     * @param event The action event
     */
    @FXML
    private void handleSchedule(ActionEvent event) {
        LocalDate appointmentDate = date.getValue();
        String timeSlot = time.getText().trim();
        String meetingLink = link.getText().trim();
        String reason = Desp.getText().trim();
        
        // Input validation
        if (appointmentDate == null || timeSlot.isEmpty() || reason.isEmpty()) {
            showErrorAlert("Scheduling Error", "Missing Information", 
                    "Please fill in all required fields.");
            return;
        }
        
        // In a real application, this would connect to a service to schedule the appointment
        // For now, we'll just simulate a successful appointment scheduling
        
        showInfoAlert("Appointment Scheduled", "Success", 
                "Your appointment on " + appointmentDate + " at " + timeSlot + " has been scheduled.");
        
        // Navigate back to the patient dashboard
        navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
    }
    
    /**
     * Handles the back button click event.
     * Navigates back to the patient dashboard.
     * 
     * @param event The action event
     */
    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
    }
} 