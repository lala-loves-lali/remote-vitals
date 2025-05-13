package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.repositories.DoctorRepository;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the patient appointments screen.
 * This screen displays available doctors and allows patients to schedule appointments.
 */
public class PatientAppointmentsController extends BaseController implements Initializable {

    @FXML
    private Label id_label;

    @FXML
    private Label name_label;

    @FXML
    private TableView<Doctor> doctorsTable;

    @FXML
    private TableColumn<Doctor, String> nameColumn;

    @FXML
    private TableColumn<Doctor, String> qualificationColumn;

    @FXML
    private TableColumn<Doctor, String> emailColumn;

    @FXML
    private TableColumn<Doctor, String> phoneColumn;

    @FXML
    private TableColumn<Doctor, Void> actionColumn;

    private Patient currentPatient;
    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    private static Doctor selectedDoctor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Get current patient
            currentPatient = getPatientUser();
            if (currentPatient == null) {
                showErrorAlert("Error", "Session Error", "No patient session found. Please log in again.");
                return;
            }

            // Set patient info
            id_label.setText("P" + currentPatient.getId());
            name_label.setText(currentPatient.getFirstName() + " " + currentPatient.getLastName());

            // Configure table columns
            setupTableColumns();

            // Load doctors
            loadDoctors();
        } catch (Exception e) {
            showErrorAlert("Error", "Initialization Error", "Failed to initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set up the table columns with appropriate cell factories
     */
    private void setupTableColumns() {
        // Name column
        nameColumn.setCellValueFactory(cellData -> {
            Doctor doctor = cellData.getValue();
            return new SimpleStringProperty(doctor.getFirstName() + " " + doctor.getLastName());
        });

        // Qualification column
        qualificationColumn.setCellValueFactory(cellData -> {
            Doctor doctor = cellData.getValue();
            String qualification = doctor.getQualificationString();
            if (qualification != null && qualification.length() > 50) {
                return new SimpleStringProperty(qualification.substring(0, 47) + "...");
            }
            return new SimpleStringProperty(qualification != null ? qualification : "");
        });

        // Add tooltip for qualification to show full text on hover
        qualificationColumn.setCellFactory(column -> {
            return new TableCell<Doctor, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        setText(item);
                        
                        Doctor doctor = getTableView().getItems().get(getIndex());
                        String fullQualification = doctor.getQualificationString();
                        
                        if (fullQualification != null && fullQualification.length() > 50) {
                            Tooltip tooltip = new Tooltip(fullQualification);
                            tooltip.setWrapText(true);
                            tooltip.setMaxWidth(400);
                            setTooltip(tooltip);
                        }
                    }
                }
            };
        });

        // Email column
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Phone column
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // Action column - Make Appointment button
        addAppointmentButtonToTable();
        
        // Set row height for better readability
        doctorsTable.setRowFactory(tv -> {
            TableRow<Doctor> row = new TableRow<>();
            row.setPrefHeight(50);
            return row;
        });
    }

    /**
     * Load doctors from the database
     */
    private void loadDoctors() {
        try {
            if (getContext() != null) {
                DoctorRepository doctorRepository = getContext().getBean(DoctorRepository.class);
                List<Doctor> doctorList = doctorRepository.findAll();
                
                if (doctorList != null && !doctorList.isEmpty()) {
                    doctors.clear();
                    doctors.addAll(doctorList);
                    doctorsTable.setItems(doctors);
                } else {
                    // Show a placeholder message if no doctors
                    Label placeholder = new Label("No doctors available at the moment");
                    placeholder.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
                    doctorsTable.setPlaceholder(placeholder);
                }
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Data Loading Error", "Failed to load doctors: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Add a "Make Appointment" button to each row in the table
     */
    private void addAppointmentButtonToTable() {
        Callback<TableColumn<Doctor, Void>, TableCell<Doctor, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Doctor, Void> call(final TableColumn<Doctor, Void> param) {
                return new TableCell<>() {
                    private final Button appointmentButton = new Button("Make Appointment");
                    {
                        appointmentButton.setOnAction((ActionEvent event) -> {
                            Doctor doctor = getTableView().getItems().get(getIndex());
                            System.out.println("Doctor selected: " + doctor.getFirstName() + " " + doctor.getId());
                            ScheduleAppointmentController.setSelectedDoctor(doctor);
                            navigateTo(event, ScreenPaths.SCHEDULE_APPOINTMENT, ScreenPaths.TITLE_SCHEDULE_APPOINTMENT);
                        });
                        
                        // Style the button
                        appointmentButton.getStyleClass().add("action-button");
                        appointmentButton.setStyle("-fx-background-color: #0066cc; -fx-text-fill: white; -fx-cursor: hand;");
                        appointmentButton.setPrefWidth(150);
                        appointmentButton.setMaxWidth(Double.MAX_VALUE);
                        appointmentButton.setPadding(new Insets(5, 10, 5, 10));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(appointmentButton);
                        }
                    }
                };
            }
        };
        
        actionColumn.setCellFactory(cellFactory);
    }

    /**
     * Handle making an appointment with the selected doctor
     * 
     * @param doctor The doctor to make an appointment with
     */
    private void handleMakeAppointment(Doctor doctor) {
        try {
            // Store the selected doctor for the appointment screen
            selectedDoctor = doctor;
            
            // Navigate to the appointment scheduling screen
            navigateTo(new ActionEvent(), ScreenPaths.SCHEDULE_APPOINTMENT, ScreenPaths.TITLE_SCHEDULE_APPOINTMENT);
        } catch (Exception e) {
            showErrorAlert("Error", "Appointment Error", "Failed to process appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get the selected doctor for use in other controllers
     * 
     * @return The selected doctor
     */
    public static Doctor getSelectedDoctor() {
        return selectedDoctor;
    }

    /**
     * Handler for the Dashboard button
     */
    @FXML
    private void handleDashboard(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
    }

    /**
     * Handler for the Profile button
     */
    @FXML
    private void handleProfile(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_PROFILE, ScreenPaths.TITLE_PATIENT_PROFILE);
    }

    /**
     * Handler for the Vitals button
     */
    @FXML
    private void handleVitals(ActionEvent event) {
        navigateTo(event, ScreenPaths.UPLOAD_VITALS, ScreenPaths.TITLE_UPLOAD_VITALS);
    }

    /**
     * Handler for the Medical History button
     */
    @FXML
    private void handleMedicalHistory(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_MEDICAL_HISTORY, ScreenPaths.TITLE_PATIENT_MEDICAL_HISTORY);
    }

    /**
     * Handler for the Logout button
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        navigateTo(event, ScreenPaths.LOGIN, ScreenPaths.TITLE_LOGIN);
    }
}
