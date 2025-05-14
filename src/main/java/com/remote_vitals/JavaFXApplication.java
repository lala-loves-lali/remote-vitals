package com.remote_vitals;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ConfigurableApplicationContext;

import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.backend.appointment.enums.AppointmentStatus;
import com.remote_vitals.backend.services.AppointmentService;
import com.remote_vitals.backend.services.CheckUpService;
import com.remote_vitals.backend.services.LoginService;
import com.remote_vitals.backend.services.SignUpService;
import com.remote_vitals.backend.services.UserService;
import com.remote_vitals.backend.user.dtos.PatientUpdateDto;
import com.remote_vitals.backend.user.entities.Admin;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.entities.User;
import com.remote_vitals.backend.user.enums.Gender;
import com.remote_vitals.backend.vital.entities.BloodPressureSystolic;
import com.remote_vitals.backend.vital.entities.BodyTemperature;
import com.remote_vitals.backend.vital.entities.HeartRate;
import com.remote_vitals.backend.vital.enums.VitalStatus;
import com.remote_vitals.backend.vitalReport.entities.VitalReport;
import com.remote_vitals.frontend.controllers.BaseController;
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
      //StaticDataClass.context1=context;
//
        // Set the database handler in BaseController first
        BaseController.setContext(context); 

        // Initialize appointment service
      

        // Initialize database with sample data
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

            Patient patient2 = new Patient(
                    "Jane", "thomas",
                    Gender.FEMALE, "954154843210",
                    "jane.thomas@example.com", "password456",
                    "No major medical history", "A-",
                    LocalDate.of(1985, 5, 15)
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
                signUpService.signUp(patient2);
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
                return; // Exit if doctor creation fails
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

            try {
                context.getBean(CheckUpService.class).submitCheckUp(patient.getId(), doctor.getId(), "Prescription", "Feedback");
            } catch (Exception e) {
            }

            // Initialize and test appointment service
            try {
                AppointmentService appointmentService = context.getBean(AppointmentService.class);
                appointmentService.addScheduleToAppointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
                System.out.println("Testing appointment service...");

                // Get all doctors (should include our dummy doctor)
                List<Doctor> doctors = appointmentService.getAllDoctors();
                if (doctors != null && !doctors.isEmpty()) {
                    Doctor testDoctor = doctors.get(0);
                    System.out.println("Found doctor: " + testDoctor.getFirstName() + " " + testDoctor.getLastName());

                    // Login as patient to set current user ID before requesting appointment
                    try {
                        loginService.login(patient.getEmail(), patient.getPassword(), Patient.class);
                        System.out.println("Logged in as patient successfully");

                        // Request an appointment
                        String requestResult = appointmentService.RequestAppointment(testDoctor.getId());
                        System.out.println("Appointment request result: " + requestResult);

                        // Get all appointments for the doctor
                        List<Appointment> doctorAppointments = appointmentService.getAllAppointments();
                        if (doctorAppointments != null && !doctorAppointments.isEmpty()) {
                            Appointment testAppointment = doctorAppointments.get(0);
                            System.out.println("Found appointment with ID: " + testAppointment.getId());

                            // Add schedule to the appointment
                            LocalDateTime startTime = LocalDateTime.now().plusDays(1);
                            LocalDateTime endTime = startTime.plusHours(1);
                            String scheduleResult = appointmentService.addScheduleToAppointment(
                                testAppointment.getId(), startTime, endTime);
                            System.out.println("Schedule addition result: " + scheduleResult);

                            // Change appointment status
                            String statusResult = appointmentService.changeAppointmentStatus(
                                testAppointment.getId(), AppointmentStatus.SCHEDULED);
                            System.out.println("Status change result: " + statusResult);

                            // Add meeting link to the appointment
                            
                            appointmentService.changeAppointmentStatus(testAppointment.getId(), AppointmentStatus.SCHEDULED);
                            appointmentService.setAppointmentLink(testAppointment.getId(), "zoom.us/j/123456789");
                            System.out.println("Added meeting link to the appointment");
                            
                            // Create additional test appointments with different meeting links
                            
                            // Create a second appointment for the same patient
                            String request2 = appointmentService.RequestAppointment(testDoctor.getId());
                            System.out.println("Second appointment request: " + request2);
                            
                            // Add schedule to the appointment
                            Optional<Appointment> latestAppointment = appointmentService.getLatestAppointment();
                            if (latestAppointment.isPresent()) {
                                Appointment appointment2 = latestAppointment.get();
                                
                                // Schedule second appointment for next week
                                LocalDateTime nextWeek = LocalDateTime.now().plusDays(7).withHour(14).withMinute(30);
                                appointmentService.addScheduleToAppointment(
                                    appointment2.getId(), nextWeek, nextWeek.plusMinutes(45));
                                
                                // Add Google Meet link
                                
                                appointmentService.changeAppointmentStatus(appointment2.getId(), AppointmentStatus.SCHEDULED);
                                appointmentService.setAppointmentLink(appointment2.getId(), "meet.google.com/abc-def-ghi");
                                System.out.println("Created second appointment with Google Meet link");
                            }
                            
                            // Login as second patient
                            loginService.login(patient2.getEmail(), patient2.getPassword(), Patient.class);
                            System.out.println("Logged in as second patient");
                            
                            // Request appointment for second patient
                            String request3 = appointmentService.RequestAppointment(testDoctor.getId());
                            System.out.println("Third appointment request: " + request3);
                            
                            Optional<Appointment> thirdAppointment = appointmentService.getLatestAppointment();
                            if (thirdAppointment.isPresent()) {
                                Appointment appointment3 = thirdAppointment.get();
                                
                                // Schedule third appointment for tomorrow
                                LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(15).withMinute(0);
                                appointmentService.addScheduleToAppointment(
                                    appointment3.getId(), tomorrow, tomorrow.plusHours(1));
                                
                                // Add Microsoft Teams link
                                appointmentService.setAppointmentLink(appointment3.getId(), "teams.microsoft.com/l/meetup-join/19%3ameeting");
                                appointmentService.changeAppointmentStatus(appointment3.getId(), AppointmentStatus.SCHEDULED);
                                System.out.println("Created third appointment with Microsoft Teams link");
                            }
                            
                            // Create an appointment with REQUESTED status
                            String request4 = appointmentService.RequestAppointment(testDoctor.getId());
                            System.out.println("Fourth appointment request: " + request4);
                            
                            // Create an appointment with POSTPONED status
                            String request5 = appointmentService.RequestAppointment(testDoctor.getId());
                            Optional<Appointment> fifthAppointment = appointmentService.getLatestAppointment();
                            if (fifthAppointment.isPresent()) {
                                Appointment appointment5 = fifthAppointment.get();
                                LocalDateTime inTwoDays = LocalDateTime.now().plusDays(2).withHour(11).withMinute(0);
                                appointmentService.addScheduleToAppointment(
                                    appointment5.getId(), inTwoDays, inTwoDays.plusMinutes(30));
                                    appointment5.setLinkForRoom("webex.com/meet/doctor");
                                    appointmentService.changeAppointmentStatus(appointment5.getId(), AppointmentStatus.POSTPONED);
                                System.out.println("Created postponed appointment with Webex link");
                            }
                            
                        } else {
                            System.out.println("No appointments found for the doctor");
                        }



                    } catch (Exception e) {
                        System.err.println("Error during appointment testing: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("No doctors found in the system");
                }
            } catch (Exception e) {
                System.err.println("Error testing appointment service: " + e.getMessage());
                e.printStackTrace();
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