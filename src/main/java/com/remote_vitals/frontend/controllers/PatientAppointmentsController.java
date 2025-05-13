package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.backend.appointment.entities.Schedule;
import com.remote_vitals.backend.appointment.enums.AppointmentStatus;
import com.remote_vitals.backend.services.AppointmentService;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.repositories.DoctorRepository;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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

    // Available Doctors Table
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

    // My Appointments Table
    @FXML
    private TableView<Appointment> myAppointmentsTable;
    
    @FXML
    private TableColumn<Appointment, String> doctorNameColumn;
    
    @FXML
    private TableColumn<Appointment, String> dateColumn;
    
    @FXML
    private TableColumn<Appointment, String> timeColumn;
    
    @FXML
    private TableColumn<Appointment, AppointmentStatus> statusColumn;
    
    @FXML
    private TableColumn<Appointment, String> meetingLinkColumn;
    
    @FXML
    private TableColumn<Appointment, Void> cancelColumn;

    private Patient currentPatient;
    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private static Doctor selectedDoctor;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private AppointmentService appointmentService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Get current patient
            currentPatient = getPatientUser();
            if (currentPatient == null) {
                showErrorAlert("Error", "Session Error", "No patient session found. Please log in again.");
                return;
            }

            // Get appointment service
            appointmentService = getContext().getBean(AppointmentService.class);

            // Set patient info
            id_label.setText("P" + currentPatient.getId());
            name_label.setText(currentPatient.getFirstName() + " " + currentPatient.getLastName());

            // Configure table columns
            setupDoctorsTableColumns();
            setupAppointmentsTableColumns();

            // Load data
            loadDoctors();
            loadAppointments();
        } catch (Exception e) {
            showErrorAlert("Error", "Initialization Error", "Failed to initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set up the doctors table columns with appropriate cell factories
     */
    private void setupDoctorsTableColumns() {
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
     * Set up the appointments table columns
     */
    private void setupAppointmentsTableColumns() {
        // Doctor Name column
        doctorNameColumn.setCellValueFactory(cellData -> {
            Doctor doctor = cellData.getValue().getDoctor();
            return new SimpleStringProperty(doctor.getFirstName() + " " + doctor.getLastName());
        });
        
        // Date column
        dateColumn.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            Schedule schedule = appointment.getSchedule();
            if (schedule != null && schedule.getStartingTime() != null) {
                return new SimpleStringProperty(schedule.getStartingTime().format(DATE_FORMATTER));
            }
            return new SimpleStringProperty("Not scheduled");
        });
        
        // Time column
        timeColumn.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            Schedule schedule = appointment.getSchedule();
            if (schedule != null && schedule.getStartingTime() != null) {
                return new SimpleStringProperty(
                    schedule.getStartingTime().format(TIME_FORMATTER) + " - " +
                    schedule.getEndingTime().format(TIME_FORMATTER)
                );
            }
            return new SimpleStringProperty("Not scheduled");
        });
        
        // Status column with color coding
        statusColumn.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().getStatus())
        );
        
        statusColumn.setCellFactory(column -> {
            return new TableCell<Appointment, AppointmentStatus>() {
                @Override
                protected void updateItem(AppointmentStatus status, boolean empty) {
                    super.updateItem(status, empty);
                    
                    if (status == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(status.toString());
                        
                        switch (status) {
                            case REQUESTED:
                                setStyle("-fx-background-color: #FFF9C4; -fx-text-fill: #F57F17;");
                                break;
                            case SCHEDULED:
                                setStyle("-fx-background-color: #E3F2FD; -fx-text-fill: #1565C0;");
                                break;
                            case REJECTED:
                                setStyle("-fx-background-color: #FFEBEE; -fx-text-fill: #C62828;");
                                break;
                            case CANCELED:
                                setStyle("-fx-background-color: #EFEBE9; -fx-text-fill: #5D4037;");
                                break;
                            case POSTPONED:
                                setStyle("-fx-background-color: #EDE7F6; -fx-text-fill: #4527A0;");
                                break;
                            default:
                                setStyle("");
                                break;
                        }
                    }
                }
            };
        });
        
        // Meeting Link column - clickable hyperlink
        meetingLinkColumn.setCellValueFactory(cellData -> {
            String link = cellData.getValue().getLinkForRoom();
            return new SimpleStringProperty(link != null ? link : "No link");
        });
        
        meetingLinkColumn.setCellFactory(column -> {
            return new TableCell<Appointment, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty || "No link".equals(item)) {
                        setText("No link");
                        setGraphic(null);
                    } else {
                        Hyperlink hyperlink = new Hyperlink("Join Meeting");
                        hyperlink.setOnAction(event -> {
                            openMeetingLink(item);
                        });
                        setGraphic(hyperlink);
                        setText(null);
                    }
                }
            };
        });
        
        // Cancel column - cancel button
        cancelColumn.setCellFactory(column -> {
            return new TableCell<Appointment, Void>() {
                private final Button cancelBtn = new Button("Cancel");
                
                {
                    // Style the button
                    cancelBtn.getStyleClass().add("danger-button");
                    cancelBtn.setPrefHeight(30);
                    
                    // Set action handlers
                    cancelBtn.setOnAction(event -> {
                        Appointment appointment = getTableView().getItems().get(getIndex());
                        handleCancelAppointment(appointment);
                    });
                }
                
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (empty) {
                        setGraphic(null);
                    } else {
                        Appointment appointment = getTableView().getItems().get(getIndex());
                        
                        // Only show cancel button for SCHEDULED or REQUESTED appointments
                        if (appointment.getStatus() == AppointmentStatus.SCHEDULED || 
                            appointment.getStatus() == AppointmentStatus.REQUESTED) {
                            setGraphic(cancelBtn);
                        } else {
                            setGraphic(null);
                        }
                    }
                }
            };
        });
        
        // Set row height for better readability
        myAppointmentsTable.setRowFactory(tv -> {
            TableRow<Appointment> row = new TableRow<>();
            row.setPrefHeight(50);
            return row;
        });
    }

    /**
     * Open a meeting link in the default browser
     */
    private void openMeetingLink(String link) {
        try {
            if (link != null && !link.isEmpty()) {
                // Ensure link has a protocol
                if (!link.startsWith("http://") && !link.startsWith("https://")) {
                    link = "https://" + link;
                }
                
                // Open in browser
                Desktop.getDesktop().browse(new URI(link));
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Browser Error", "Could not open the meeting link: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Handle canceling an appointment
     */
    private void handleCancelAppointment(Appointment appointment) {
        try {
            // Confirm cancellation
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Cancel Appointment");
            confirmAlert.setHeaderText("Cancel Appointment Confirmation");
            confirmAlert.setContentText("Are you sure you want to cancel this appointment?");
            
            // Show confirmation dialog and wait for response
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Cancel the appointment through the service
                    String result = appointmentService.changeAppointmentStatus(
                            appointment.getId(), AppointmentStatus.CANCELED);
                    
                    if (result.contains("Successfully")) {
                        // Refresh the appointments list
                        loadAppointments();
                        
                        // Show success message
                        showInfoAlert("Success", "Appointment Canceled", 
                                "Your appointment has been successfully canceled.");
                    } else {
                        showErrorAlert("Error", "Cancellation Failed", result);
                    }
                }
            });
        } catch (Exception e) {
            showErrorAlert("Error", "Cancellation Error", "Failed to cancel appointment: " + e.getMessage());
            e.printStackTrace();
        }
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
     * Load patient's appointments
     */
    private void loadAppointments() {
        try {
            if (appointmentService != null) {
                List<Appointment> appointmentList = appointmentService.getAllPatientAppointments();
                
                if (appointmentList != null && !appointmentList.isEmpty()) {
                    appointments.clear();
                    appointments.addAll(appointmentList);
                    myAppointmentsTable.setItems(appointments);
                } else {
                    // Show a placeholder message if no appointments
                    Label placeholder = new Label("You have no appointments scheduled");
                    placeholder.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
                    myAppointmentsTable.setPlaceholder(placeholder);
                }
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Data Loading Error", "Failed to load appointments: " + e.getMessage());
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
