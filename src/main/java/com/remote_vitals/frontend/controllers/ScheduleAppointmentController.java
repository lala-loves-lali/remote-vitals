package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.backend.appointment.enums.AppointmentStatus;
import com.remote_vitals.backend.services.AppointmentService;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for scheduling appointments with doctors.
 * This screen allows patients to select a date and time for their appointment.
 */
public class ScheduleAppointmentController extends BaseController implements Initializable {

    @FXML
    private Button back_button;

    @FXML
    private Button home_button;

    @FXML
    private DatePicker Date;

    @FXML
    private ChoiceBox<String> start_hour;

    @FXML
    private ChoiceBox<String> start_minute;

    @FXML
    private ChoiceBox<String> stop_hor;

    @FXML
    private ChoiceBox<String> stop_minute;

    // The selected doctor from the previous screen
    private static Doctor selectedDoctor;
    private Patient currentPatient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("Initializing ScheduleAppointmentController");
            // Get the current patient
            currentPatient = getPatientUser();
            if (currentPatient == null) {
                showErrorAlert("Error", "Session Error", "No patient session found. Please log in again.");
                return;
            }

            // Debug: Print info about selected doctor
            System.out.println("Selected doctor info: " + 
                (selectedDoctor != null ? 
                "ID: " + selectedDoctor.getId() + 
                ", Name: " + selectedDoctor.getFirstName() + " " + selectedDoctor.getLastName() +
                ", Email: " + selectedDoctor.getEmail() : "null"));

            // Get the selected doctor from the PatientAppointmentsController
            if (selectedDoctor == null) {
                showErrorAlert("Error", "Doctor Selection", "No doctor was selected. Please go back and select a doctor.");
                return;
            }

            // Set up hour and minute dropdowns
            setupTimeDropdowns();

            // Set up the date picker to disable past dates
            Date.setValue(LocalDate.now());
            Date.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 0);
                }
            });

            // Set up button handlers
            setupButtonHandlers();

        } catch (Exception e) {
            showErrorAlert("Error", "Initialization Error", "Failed to initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set up the hour and minute dropdowns
     */
    private void setupTimeDropdowns() {
        // Hours (0-23)
        List<String> hours = new ArrayList<>();
        for (int i = 8; i <= 20; i++) {
            hours.add(String.format("%02d", i));
        }
        start_hour.setItems(FXCollections.observableArrayList(hours));
        stop_hor.setItems(FXCollections.observableArrayList(hours));
        
        // Minutes (0-59, but typically appointments are on quarter hours)
        List<String> minutes = new ArrayList<>();
        minutes.add("00");
        minutes.add("15");
        minutes.add("30");
        minutes.add("45");
        start_minute.setItems(FXCollections.observableArrayList(minutes));
        stop_minute.setItems(FXCollections.observableArrayList(minutes));
        
        // Set default values
        start_hour.setValue("09");
        start_minute.setValue("00");
        stop_hor.setValue("10");
        stop_minute.setValue("00");
    }

    /**
     * Set up button handlers
     */
    private void setupButtonHandlers() {
        // Back button handler
        back_button.setOnAction(event -> handleBack(event));
        
        // Home button handler
        home_button.setOnAction(event -> handleDashboard(event));
    }

    /**
     * Handle the schedule button action
     */
    @FXML
    private void handleSchedule(ActionEvent event) {
        try {
            // Debug: Print info about selected doctor
            System.out.println("Schedule button clicked with doctor: " + 
                (selectedDoctor != null ? 
                "ID: " + selectedDoctor.getId() + 
                ", Name: " + selectedDoctor.getFirstName() + " " + selectedDoctor.getLastName() : "null"));
            
            // Get the selected date and times
            LocalDate selectedDate = Date.getValue();
            
            if (selectedDate == null) {
                showErrorAlert("Error", "Date Required", "Please select a date for your appointment.");
                return;
            }
            
            // Get hours and minutes
            int startHour, startMinute, endHour, endMinute;
            
            try {
                startHour = Integer.parseInt(start_hour.getValue());
                startMinute = Integer.parseInt(start_minute.getValue());
                endHour = Integer.parseInt(stop_hor.getValue());
                endMinute = Integer.parseInt(stop_minute.getValue());
            } catch (NumberFormatException e) {
                showErrorAlert("Error", "Time Required", "Please select valid start and end times.");
                return;
            }
            
            // Create LocalDateTime objects
            LocalDateTime startDateTime = LocalDateTime.of(
                    selectedDate, 
                    LocalTime.of(startHour, startMinute)
            );
            
            LocalDateTime endDateTime = LocalDateTime.of(
                    selectedDate, 
                    LocalTime.of(endHour, endMinute)
            );
            
            // Validate that end time is after start time
            if (!endDateTime.isAfter(startDateTime)) {
                showErrorAlert("Error", "Invalid Time Range", "End time must be after start time.");
                return;
            }
            
            // Validate that appointment is not in the past
            if (startDateTime.isBefore(LocalDateTime.now())) {
                showErrorAlert("Error", "Invalid Time", "Appointment time cannot be in the past.");
                return;
            }
            
            // Request the appointment using the AppointmentService
            AppointmentService appointmentService = getContext().getBean(AppointmentService.class);
            
            // First request the appointment to create it
            String requestResult = appointmentService.RequestAppointment(selectedDoctor.getId());
            System.out.println("Appointment request result: " + requestResult);
            
            if (requestResult.contains("Successfully")) {
                // Get the latest appointment using the new method
                Optional<Appointment> latestAppointment = appointmentService.getLatestAppointment();
                System.out.println("Latest appointment found: " + latestAppointment.isPresent());
                
                if (latestAppointment.isPresent()) {
                    Appointment newAppointment = latestAppointment.get();
                    System.out.println("Using appointment ID: " + newAppointment.getId());
                    
                    // Add schedule to the appointment
                    String scheduleResult = appointmentService.addScheduleToAppointment(
                            newAppointment.getId(), startDateTime, endDateTime);
                    System.out.println("Schedule result: " + scheduleResult);
                    
                    if (scheduleResult.contains("Successfully")) {
                        showInfoAlert("Success", "Appointment Scheduled", 
                                "Your appointment with Dr. " + selectedDoctor.getFirstName() + " " + 
                                selectedDoctor.getLastName() + " has been scheduled for " + 
                                startDateTime.toLocalDate() + " at " + startDateTime.toLocalTime() + ".");
                        
                        // Navigate back to the patient dashboard
                        navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
                    } else {
                        showErrorAlert("Error", "Schedule Error", "Could not schedule appointment: " + scheduleResult);
                    }
                } else {
                    showErrorAlert("Error", "Appointment Creation", "Appointment was requested but could not be retrieved.");
                }
            } else {
                showErrorAlert("Error", "Appointment Request", "Could not request appointment: " + requestResult);
            }
            
        } catch (Exception e) {
            showErrorAlert("Error", "Scheduling Error", "Failed to schedule appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle the back button action
     */
    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
    }

    /**
     * Handle the home button action
     */
    @FXML
    private void handleDashboard(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
    }

    public static void setSelectedDoctor(Doctor doctor) {
        System.out.println("Setting selected doctor: " + (doctor != null ? 
            "ID: " + doctor.getId() + ", Name: " + doctor.getFirstName() + " " + doctor.getLastName() : "null"));
        selectedDoctor = doctor;
    }
}

