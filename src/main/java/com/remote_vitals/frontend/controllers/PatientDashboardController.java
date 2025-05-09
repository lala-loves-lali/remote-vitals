package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import javafx.geometry.Insets;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the patient dashboard screen.
 * Handles patient-specific functionality and navigation.
 */
public class PatientDashboardController extends BaseController implements Initializable {

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
    private VBox doctorInfoBox;

    @FXML
    private Label doctorNameLabel;

    @FXML
    private Label doctorEmailLabel;

    @FXML
    private Label doctorPhoneLabel;
    
    @FXML
    private Label doctorQualificationLabel;

    @FXML
    private Button removeDoctorButton;

    @FXML
    private VBox availableDoctorsBox;

    @FXML
    private TableView<Doctor> doctorsTable;

    @FXML
    private TableColumn<Doctor, String> doctorNameColumn;

    @FXML
    private TableColumn<Doctor, String> doctorEmailColumn;

    @FXML
    private TableColumn<Doctor, String> doctorPhoneColumn;

    @FXML
    private TableColumn<Doctor, String> doctorQualificationColumn;
    
    @FXML
    private TableColumn<Doctor, Void> doctorActionColumn;

    private Patient currentPatient;
    private ObservableList<Doctor> availableDoctors;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("Initializing PatientDashboardController...");
            
            // Check if UI components were properly injected
            if (doctorNameLabel == null) {
                System.out.println("Error: doctorNameLabel not initialized. FXML loading issue.");
                return;
            }
            
            // Get current patient
            currentPatient = getPatientUser();
            System.out.println("Current patient: " + (currentPatient != null ? currentPatient.getFirstName() : "null"));
            
            if (currentPatient == null) {
                System.out.println("No patient found. UserType: " + getUserType());
                System.out.println("Current user: " + (getCurrentUser() != null ? getCurrentUser().getClass().getSimpleName() : "null"));
                showErrorAlert("Error", "Session Error", "No patient session found. Please log in again.");
               
            }

            // Set welcome message
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, " + currentPatient.getFirstName());
                System.out.println("Set welcome message for: " + currentPatient.getFirstName());
            } else {
                System.out.println("Warning: welcomeLabel is null. Check your FXML file for the element with fx:id='welcomeLabel'");
            }

            // Initialize doctor table
            if (doctorsTable != null && doctorNameColumn != null && doctorEmailColumn != null && 
                doctorPhoneColumn != null && doctorActionColumn != null) {
                initializeDoctorTable();
            } else {
                System.out.println("Error: Table or its columns not initialized. Check FXML.");
            }
            
            // Load doctor information
            if (doctorNameLabel != null && doctorEmailLabel != null && doctorPhoneLabel != null && 
                removeDoctorButton != null && availableDoctorsBox != null) {
                loadDoctorInformation();
            } else {
                System.out.println("Error: Doctor info UI elements not initialized. Check FXML.");
            }
            
            System.out.println("PatientDashboardController initialization complete");
        } catch (Exception e) {
            System.out.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Initializes the doctors table with columns and data
     */
    private void initializeDoctorTable() {
        System.out.println("Initializing doctor table...");

        try {
            System.out.println("Initializing doctor table...");
            
            // Set up table columns
            doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            doctorEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            doctorPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            doctorQualificationColumn.setCellValueFactory(new PropertyValueFactory<>("qualificationString"));
            
            System.out.println("Doctor table columns configured");
            
            // Add assign button to each row
            addButtonToTable();
            System.out.println("Added assign buttons to table");
            
            // Load available doctors
            loadAvailableDoctors();
            
            // Add click listener to table rows for debugging
            doctorsTable.setRowFactory(tv -> {
                TableRow<Doctor> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty()) {
                        Doctor doctor = row.getItem();
                        System.out.println("Row clicked: " + doctor.getFirstName() + " " + doctor.getLastName());
                    }
                });
                return row;
            });
            
            System.out.println("Doctor table initialization complete");
        } catch (Exception e) {
            System.out.println("Error initializing doctor table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Adds an Assign button to each row in the doctors table
     */
    private void addButtonToTable() {
        System.out.println("Adding assign button to table...");
        Callback<TableColumn<Doctor, Void>, TableCell<Doctor, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Doctor, Void> call(final TableColumn<Doctor, Void> param) {
                return new TableCell<>() {
                    private final Button assignButton = new Button("Assign");
                    
                    {
                        assignButton.setOnAction((ActionEvent event) -> {
                            try {
                                Doctor doctor = getTableView().getItems().get(getIndex());
                                System.out.println("Assign button clicked for doctor: " + doctor.getFirstName());
                                



                                // Show confirmation dialog
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Assign Doctor");
                                alert.setHeaderText("Confirm Doctor Assignment");
                                alert.setContentText("Are you sure you want to assign Dr. " + 
                                                   doctor.getFirstName() + " " + doctor.getLastName() + 
                                                   " as your doctor?");
                                getPatientUser().setAssignedDoctor(getDb().getDoctor(doctor.getId()));
                                getDb().updatePatient(getPatientUser());
                                setCurrentUser(getPatientUser());
                                System.out.println("Patient assigned to doctor: " + getPatientUser().getAssignedDoctor().getFirstName());
                                System.out.println("Doctor assigned to patient: " + doctor.getFirstName());
                                navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);

                            } catch (Exception e) {
                                System.out.println("Error in assign button action: " + e.getMessage());
                                e.printStackTrace();
                                showErrorAlert("Error", "Button Error", 
                                        "An error occurred when clicking the assign button: " + e.getMessage());
                            }
                        });
                        
                        // Make the button visually distinct
                        assignButton.getStyleClass().add("action-button");
                        assignButton.setStyle("-fx-background-color: #0066cc; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                        assignButton.setPrefWidth(80);
                        assignButton.setMaxWidth(Double.MAX_VALUE);
                        assignButton.setPadding(new Insets(5, 10, 5, 10));
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(assignButton);
                        }
                    }
                };
            }
        };
        
        doctorActionColumn.setCellFactory(cellFactory);
    }

    /**
     * Loads the list of available doctors
     */
    private void loadAvailableDoctors() {
        try {
            System.out.println("Loading available doctors...");
            
            if (getDb() == null) {
                System.out.println("Error: Database handler is null");
                showErrorAlert("Error", "Database Error", "Database connection not established.");
                return;
            }
            
            List<Doctor> doctors = getDb().getAllDoctors();
            System.out.println("Fetched doctors: " + (doctors != null ? doctors.size() : "null"));
            
            if (doctors == null || doctors.isEmpty()) {
                System.out.println("No doctors found in database");
                availableDoctors = FXCollections.observableArrayList();
            } else {
                for (Doctor doctor : doctors) {
                    System.out.println("Doctor: " + doctor.getFirstName() + " " + doctor.getLastName());
                }
                availableDoctors = FXCollections.observableArrayList(doctors);
            }
            
            doctorsTable.setItems(availableDoctors);
            System.out.println("Doctors table updated");
        } catch (Exception e) {
            System.out.println("Error loading doctors: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Loads and displays the current doctor's information
     */
    private void loadDoctorInformation() {
        try {
            System.out.println("Loading doctor information...");
            
            if (currentPatient == null) {
                System.out.println("Error: Current patient is null");
                return;
            }
            
            Doctor assignedDoctor = currentPatient.getAssignedDoctor();
            System.out.println("Assigned doctor: " + (assignedDoctor != null ? 
                    assignedDoctor.getFirstName() + " " + assignedDoctor.getLastName() : "null"));
            
            if (assignedDoctor != null) {
                doctorNameLabel.setText(assignedDoctor.getFirstName() + " " + assignedDoctor.getLastName());
                doctorEmailLabel.setText("Email: " + assignedDoctor.getEmail());
                doctorPhoneLabel.setText("Phone: " + assignedDoctor.getPhoneNumber());
                
                // Display qualification
                String qualification = assignedDoctor.getQualificationString();
                if (qualification != null && !qualification.isEmpty()) {
                    doctorQualificationLabel.setText("Qualification: " + qualification);
                    doctorQualificationLabel.setVisible(true);
                } else {
                    doctorQualificationLabel.setText("No qualification specified");
                    doctorQualificationLabel.setVisible(true);
                }
                
                removeDoctorButton.setVisible(true);
                
                // Hide available doctors section when a doctor is already assigned
                availableDoctorsBox.setVisible(false);
                System.out.println("Doctor information loaded, hiding available doctors");
            } else {
                doctorNameLabel.setText("No doctor assigned");
                doctorEmailLabel.setText("");
                doctorPhoneLabel.setText("");
                doctorQualificationLabel.setText("");
                doctorQualificationLabel.setVisible(false);
                removeDoctorButton.setVisible(false);
                
                // Show available doctors section when no doctor is assigned
                availableDoctorsBox.setVisible(true);
                System.out.println("No doctor assigned, showing available doctors");
            }
        } catch (Exception e) {
            System.out.println("Error loading doctor information: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleRemoveDoctor(ActionEvent event) { 
        System.out.println("Removing doctor...");
            
             try{
                    System.out.println("Removing doctor from database...");
                    getDb().removePatientFromDoctor(getPatientUser());
                    setCurrentUser(getDb().getPatient(getPatientUser().getId()));
                    System.out.println("Patient set as current user");
                    System.out.println("Current user: " + getCurrentUser().getFirstName());
                    System.out.println("Doctor removed from database");
                   

                    navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
                } catch (Exception e) {
                    showErrorAlert("Error", "Removal failed", 
                            e.getMessage());
                }
            }
  
    
    /**
     * Handles the upload vitals button click event.
     * Navigates to the upload vitals screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleUploadVitals(ActionEvent event) {
        navigateTo(event, ScreenPaths.VIEW_VITALS, ScreenPaths.TITLE_VIEW_VITALS);
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