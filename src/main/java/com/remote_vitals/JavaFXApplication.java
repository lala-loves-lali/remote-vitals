package com.remote_vitals;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import com.remote_vitals.backend.services.LoginService;
import com.remote_vitals.backend.services.SignUpService;
import com.remote_vitals.backend.services.UserService;
import com.remote_vitals.backend.user.dtos.PatientUpdateDto;
import com.remote_vitals.backend.user.entities.Admin;
import com.remote_vitals.backend.user.entities.User;
import com.remote_vitals.backend.vital.enums.VitalStatus;
import com.remote_vitals.backend.vitalReport.entities.VitalReport;
import com.remote_vitals.frontend.controllers.BaseController;
import org.springframework.context.ConfigurableApplicationContext;

import com.remote_vitals.backend.appointment.entities.Appointment;
//import com.remote_vitals.backend.chat.entities.ChatRoom;
import com.remote_vitals.backend.checkup.entities.CheckUp;


import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;

import com.remote_vitals.backend.user.enums.Gender;
import com.remote_vitals.backend.vital.entities.BloodPressureSystolic;
import com.remote_vitals.backend.vital.entities.BodyTemperature;
import com.remote_vitals.backend.vital.entities.HeartRate;
import com.remote_vitals.backend.vital.entities.VitalRecord;
import com.remote_vitals.frontend.utils.ScreenPaths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX Application entry point
 * This class handles the initialization and startup of the JavaFX application.
 * It integrates Spring Boot with JavaFX and initializes the database with dummy data.
 */
public class JavaFXApplication extends Application {

    /** Spring application context for dependency injection */
    private ConfigurableApplicationContext context;

    /**
     * Initialization method called before the application starts
     * Sets up Spring context and initializes database with dummy data
     */
    @Override
    public void init() throws Exception {
        // Initialize Spring Boot context
      context = org.springframework.boot.SpringApplication.run(RemoteVitalsApplication.class);
//
//        // Set the database handler in BaseController first
        BaseController.setContext(context); 
//
//        // Initialize database with sample data
        initializeDummyData();


        System.out.println("**********************************************************");
    }
    
    /**
     * Main application start method
     * Loads the initial login screen
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Start with login screen for real-world application flow
        loadScreen(stage, ScreenPaths.LOGIN_PAGE, ScreenPaths.TITLE_LOGIN);
        
        // Other options for development/testing:
        // - Start with dashboard selector for testing
        // loadScreen(stage, ScreenPaths.DASHBOARD_SELECTOR, ScreenPaths.TITLE_DASHBOARD_SELECTOR);
        
        // - Start directly with a specific dashboard
        // loadScreen(stage, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
    }
    
    /**
     * Helper method to load a screen
     * Creates a new scene from FXML and sets it as the current stage
     * 
     * @param stage The stage to load the screen into
     * @param screenPath The path to the FXML file
     * @param title The title for the window
     * @throws IOException If the FXML file cannot be loaded
     */
    private void loadScreen(Stage stage, String screenPath, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFXApplication.class.getResource(screenPath));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the database with sample data for testing and development
     * Creates dummy doctors, patients, vital records, checkups, and appointments
     */
    private void initializeDummyData() {
        System.out.println("Javafx ");

        try {
            // Get services
            SignUpService signUpService = context.getBean(SignUpService.class);
            UserService userService = context.getBean(UserService.class);
            LoginService loginService = context.getBean(LoginService.class);

            // Create test users
            Patient patient = new Patient(
                    "John", "Doe",
                    Gender.MALE, "1234567890",
                    "john.doe@example.com", "password123",
                    "No major medical history", "A+",
                    LocalDate.of(1990, 1, 1)
            );

            Doctor doctor = new Doctor(
                    "Jane", "Smith",
                    Gender.FEMALE, "9876543210",
                    "jane.smith@example.com", "password456",
                    "MD, Cardiology", "Experienced cardiologist"
            );

            Admin admin = new Admin(
                    "Admin", "User",
                    Gender.OTHER, "5555555555",
                    "admin@example.com", "admin123"
            );

            // Save users to database with error handling
            try {
                signUpService.signUp(patient);
                System.out.println("Patient created successfully");
            } catch (Exception e) {
                System.err.println("Error creating patient: " + e.getMessage());
                return; // Exit if patient creation fails
            }

            try {
                signUpService.signUp(doctor);
                System.out.println("Doctor created successfully");
            } catch (Exception e) {
                System.err.println("Error creating doctor: " + e.getMessage());
            }

            try {
                signUpService.signUp(admin);
                System.out.println("Admin created successfully");
            } catch (Exception e) {
                System.err.println("Error creating admin: " + e.getMessage());
            }

            // Login as patient to set current user ID
            try {
                loginService.login(patient.getEmail(), patient.getPassword(), Patient.class);
                System.out.println("Logged in as patient successfully");
            } catch (Exception e) {
                System.err.println("Error logging in as patient: " + e.getMessage());
                return;
            }

            // Create test vital records with error handling
            try {
                HeartRate heartRate = new HeartRate();
                heartRate.setValue(75);
                heartRate.setStatus(VitalStatus.NORMAL);

                BloodPressureSystolic bloodPressure = new BloodPressureSystolic();
                bloodPressure.setValue(120);
                bloodPressure.setStatus(VitalStatus.NORMAL);

                BodyTemperature temperature = new BodyTemperature();
                temperature.setValue(37.0f);
                temperature.setStatus(VitalStatus.NORMAL);

                // Create vital report
                VitalReport report = new VitalReport();
                report.setReportWhenMade(LocalDateTime.now());
                report.getVitalRecords().add(heartRate);
                report.getVitalRecords().add(bloodPressure);
                report.getVitalRecords().add(temperature);

                // Get current user (which should be the patient we just logged in as)
                Optional<User> currentUser = userService.getCurrentUser();
                if (currentUser.isPresent() && currentUser.get() instanceof Patient) {
                    Patient dbPatient = (Patient) currentUser.get();

                    // Associate report with patient
                    dbPatient.getVitalReports().add(report);

                    // Update patient with new report
                    PatientUpdateDto updateDto = new PatientUpdateDto(
                            dbPatient.getPhoneNumber(),
                            dbPatient.getPnVisibility(),
                            dbPatient.getEmail(),
                            dbPatient.getEVisibility(),
                            dbPatient.getPassword(),
                            dbPatient.getMedicalHistory(),
                            dbPatient.getNextOfKinEmail()
                    );
                    userService.updateUser(updateDto);
                    System.out.println("Patient updated successfully");
                } else {
                    System.err.println("Error: Could not find current user or user is not a patient");
                }
            } catch (Exception e) {
                System.err.println("Error creating vital records: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error in initializeDummyData: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("ending");
    }

    /**
     * Main method to launch the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}