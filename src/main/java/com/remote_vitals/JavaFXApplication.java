package com.remote_vitals;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.remote_vitals.frontend.controllers.BaseController;
import org.springframework.context.ConfigurableApplicationContext;

import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.backend.appointment.entities.Schedule;
//import com.remote_vitals.backend.chat.entities.ChatRoom;
import com.remote_vitals.backend.checkup.entities.CheckUp;

import com.remote_vitals.backend.db_handler.DataBaseHandler;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.entities.Qualification;
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
        
        // Set the database handler in BaseController first
        BaseController.setDb(context.getBean(DataBaseHandler.class));
        
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
        // Create dummy doctors with their specialties
        Doctor doctor1 = new Doctor("John", "Smith", Gender.MALE, "1234567890", "john.smith@hospital.com", "password123");
        doctor1.setDescription("Cardiologist with 10 years of experience");
        BaseController.getDb().registerDoctor(doctor1);

        Doctor doctor2 = new Doctor("Sarah", "Johnson", Gender.FEMALE, "9876543210", "sarah.j@hospital.com", "password456");
        doctor2.setDescription("Pediatrician specializing in child development");
        BaseController.getDb().registerDoctor(doctor2);

        // Create dummy patients with their medical information
        Patient patient1 = new Patient("Michael", "Brown", Gender.MALE, "5551234567", "michael.b@email.com", "password789", "description", "A+", LocalDateTime.now());
        BaseController.getDb().registerPatient(patient1);

        Patient patient2 = new Patient("Emily", "Davis", Gender.FEMALE, "5559876543", "emily.d@email.com", "password012", "description", "B-", LocalDateTime.now());
        BaseController.getDb().registerPatient(patient2);

        // Add professional qualifications for doctors
        Qualification qual1 = new Qualification("MD in Cardiology", doctor1);
        // BaseController.getDb().addQualification(qual1);

        Qualification qual2 = new Qualification();
        qual2.setLabel("Pediatric Specialist");
        qual2.setDoctor(doctor2);
        // BaseController.getDb().addQualification(qual2);

        // Add sample vital records for patients
        ArrayList<VitalRecord> vitals1 = new ArrayList<>();
        vitals1.add(new BloodPressureSystolic(90));
        vitals1.add(new HeartRate(75));
        vitals1.add(new BodyTemperature(33));
        BaseController.getDb().addVitalReport(patient1, LocalDateTime.now(), vitals1);

        ArrayList<VitalRecord> vitals2 = new ArrayList<>();
        vitals2.add(new BloodPressureSystolic(90));
        vitals2.add(new HeartRate(72));
        vitals2.add(new BodyTemperature(33));
        BaseController.getDb().addVitalReport(patient2, LocalDateTime.now(), vitals2);

        // Add sample medical checkups
        CheckUp checkup1 = new CheckUp("feed", "description", LocalDateTime.now(), patient1, doctor1);
        BaseController.getDb().addCheckUp(checkup1);

        CheckUp checkup2 = new CheckUp("feed", "description", LocalDateTime.now(), patient2, doctor2);
        BaseController.getDb().addCheckUp(checkup2);

        // Create schedules for appointments
        Schedule schedule1 = new Schedule(LocalDateTime.now().plusDays(7), LocalDateTime.now().plusDays(7).plusHours(1), new Appointment());
        // BaseController.getDb().addSchedule(schedule1);

        Schedule schedule2 = new Schedule(LocalDateTime.now().plusDays(14), LocalDateTime.now().plusDays(14).plusHours(1), new Appointment());
        // BaseController.getDb().addSchedule(schedule2);

        // Create sample appointments with chat rooms
        Appointment appointment1 = new Appointment(patient1, doctor1, schedule1);
        // BaseController.getDb().addAppointment(appointment1);

        Appointment appointment2 = new Appointment(patient2, doctor2, schedule2);
        // BaseController.getDb().addAppointment(appointment2);
    }

    /**
     * Main method to launch the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}