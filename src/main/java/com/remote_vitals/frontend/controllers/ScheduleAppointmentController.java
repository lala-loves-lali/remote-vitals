package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controller for the schedule appointment screen.
 * Handles scheduling and managing patient appointments.
 */
public class ScheduleAppointmentController extends BaseController {

    @FXML
    private TextField link;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private TextField timeField;
    
    @FXML
    private TextField durationField;
    
    @FXML
    private TextArea reasonTextArea;
    
    @FXML
    private ComboBox<Doctor> doctorComboBox;
    
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
        if (datePicker != null) {
            datePicker.setValue(LocalDate.now().plusDays(1));
        }
        
        // Set default time
        if (timeField != null) {
            timeField.setText("09:00");
        }
        
        // Set default duration (in minutes)
        if (durationField != null) {
            durationField.setText("60");
        }
        
        // Load available doctors
        loadDoctors();
    }
    
    /**
     * Loads the list of available doctors for appointment scheduling
     */
    private void loadDoctors() {
//        try {
//            List<Doctor> doctors = getDb().getAllDoctors();
//            if (doctors != null && !doctors.isEmpty()) {
//                doctorComboBox.getItems().clear();
//                doctorComboBox.getItems().addAll(doctors);
//            } else {
//                showErrorAlert("Error", "No Doctors Available",
//                        "There are no doctors available for scheduling appointments.");
//            }
//        } catch (Exception e) {
//            showErrorAlert("Error", "Failed to Load Doctors",
//                    "An error occurred while loading the list of doctors: " + e.getMessage());
//        }
    }
    
    /**
     * Handles the schedule button click event.
     * Schedules the appointment and navigates back to the patient dashboard.
     * 
     * @param event The action event
     */
    @FXML
    private void handleSchedule(ActionEvent event) {
//        try {
//            // Validate inputs
//            LocalDate appointmentDate = datePicker.getValue();
//            String timeString = timeField.getText().trim();
//            String durationString = durationField.getText().trim();
//            String meetingLink = link.getText().trim();
//            String reason = reasonTextArea.getText().trim();
//            Doctor selectedDoctor = doctorComboBox.getValue();
//
//            // Input validation
//            if (appointmentDate == null || timeString.isEmpty() || durationString.isEmpty() ||
//                    meetingLink.isEmpty() || reason.isEmpty() || selectedDoctor == null) {
//                showErrorAlert("Scheduling Error", "Missing Information",
//                        "Please fill in all required fields.");
//                return;
//            }
//
//            // Parse time string (format: HH:mm)
//            LocalTime appointmentTime;
//            try {
//                appointmentTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
//            } catch (DateTimeParseException e) {
//                showErrorAlert("Scheduling Error", "Invalid Time Format",
//                        "Please enter the time in HH:MM format (e.g., 09:30).");
//                return;
//            }
//
//            // Parse duration
//            int durationMinutes;
//            try {
//                durationMinutes = Integer.parseInt(durationString);
//                if (durationMinutes <= 0) {
//                    throw new NumberFormatException("Duration must be positive");
//                }
//            } catch (NumberFormatException e) {
//                showErrorAlert("Scheduling Error", "Invalid Duration",
//                        "Please enter a valid positive number for the duration in minutes.");
//                return;
//            }
//
//            // Create start and end times
//            LocalDateTime startTime = LocalDateTime.of(appointmentDate, appointmentTime);
//            LocalDateTime endTime = startTime.plusMinutes(durationMinutes);
//
//            // Get the current patient
//            Patient currentPatient = getPatientUser();
//            if (currentPatient == null) {
//                showErrorAlert("Scheduling Error", "No Patient Session",
//                        "Patient session not found. Please log in again.");
//                return;
//            }
//
//            // Create appointment
//            int result = getDb().placeAppointmentRequest(
//                    currentPatient,
//                    selectedDoctor,
//                    startTime,
//                    endTime,
//                    meetingLink);
//
//            if (result > 0) {
//                showInfoAlert("Appointment Scheduled", "Success",
//                        "Your appointment with Dr. " + selectedDoctor.getFirstName() + " " +
//                        selectedDoctor.getLastName() + " on " + appointmentDate +
//                        " at " + appointmentTime + " has been scheduled.");
//
//                // Navigate back to the patient dashboard
//                navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
//            } else {
//                showErrorAlert("Scheduling Error", "Failed to Schedule",
//                        "Failed to schedule the appointment. Please try again later.");
//            }
//        } catch (Exception e) {
//            showErrorAlert("Scheduling Error", "Unexpected Error",
//                    "An unexpected error occurred: " + e.getMessage());
//            e.printStackTrace();
//        }
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
    
    /**
     * Handles the upload vitals button click event.
     * Navigates to the view vitals screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleUploadVitals(ActionEvent event) {
        navigateTo(event, ScreenPaths.VIEW_VITALS, ScreenPaths.TITLE_VIEW_VITALS);
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