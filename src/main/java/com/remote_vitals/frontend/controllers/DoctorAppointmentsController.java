package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.backend.appointment.entities.Schedule;
import com.remote_vitals.backend.appointment.enums.AppointmentStatus;
import com.remote_vitals.backend.services.AppointmentService;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class DoctorAppointmentsController extends BaseController implements Initializable {
    
    @FXML
    private TableView<Appointment> appointmentsTable;
    
    @FXML
    private TableColumn<Appointment, String> patientNameColumn;
    
    @FXML
    private TableColumn<Appointment, String> dateColumn;
    
    @FXML
    private TableColumn<Appointment, String> timeColumn;
    
    @FXML
    private TableColumn<Appointment, String> linkColumn;
    
    @FXML
    private TableColumn<Appointment, AppointmentStatus> statusColumn;
    
    @FXML
    private TableColumn<Appointment, Appointment> actionsColumn;
    
    @FXML
    private Button acceptButton;
    
    @FXML
    private Button rejectButton;
    
    @FXML
    private Button completeButton;
    
    @FXML
    private Button rescheduleButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private Button resetButton;
    
    @FXML
    private Button updateLinkButton;
    
    @FXML
    private ComboBox<String> statusFilterComboBox;
    
    @FXML
    private CheckBox futureDateCheckBox;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private TextField linkTextField;
    
    @FXML
    private VBox appointmentDetailsPane;
    
    @FXML
    private Label detailsPatientLabel;
    
    @FXML
    private Label detailsDateTimeLabel;
    
    @FXML
    private Label detailsStatusLabel;
    
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private FilteredList<Appointment> filteredAppointments;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    private AppointmentService appointmentService;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Get appointment service
            appointmentService = getContext().getBean(AppointmentService.class);
            
            // Set up table columns
            setupTableColumns();
            
            // Set up filter options
            setupFilters();
            
            // Load appointments
            loadAppointments();
            
            // Initially hide appointment details until something is selected
            appointmentDetailsPane.setVisible(false);
            
            // Add selection listener to the table
            appointmentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                updateButtonStates(newSelection);
                showAppointmentDetails(newSelection);
            });
            
        } catch (Exception e) {
            showErrorAlert("Error", "Initialization Error", "Failed to initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Set up the table columns
     */
    private void setupTableColumns() {
        // Patient Name column
        patientNameColumn.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue().getPatient();
            return new SimpleStringProperty(patient.getFirstName() + " " + patient.getLastName());
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
        
        // Link column - clickable hyperlink
        linkColumn.setCellValueFactory(cellData -> {
            String link = cellData.getValue().getLinkForRoom();
            return new SimpleStringProperty(link != null ? link : "No link");
        });
        
        linkColumn.setCellFactory(column -> {
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
        
        // Actions column - quick action buttons
        actionsColumn.setCellFactory(column -> {
            return new TableCell<Appointment, Appointment>() {
                private final Button acceptBtn = new Button("Accept");
                private final Button rejectBtn = new Button("Reject");
                
                {
                    // Style buttons
                    acceptBtn.getStyleClass().add("primary-button");
                    acceptBtn.setPrefWidth(70);
                    acceptBtn.setMinWidth(70);
                    acceptBtn.setPrefHeight(30);
                    
                    rejectBtn.getStyleClass().add("danger-button");
                    rejectBtn.setPrefWidth(70);
                    rejectBtn.setMinWidth(70);
                    rejectBtn.setPrefHeight(30);
                    
                    // Set action handlers
                    acceptBtn.setOnAction(event -> {
                        Appointment appointment = getTableView().getItems().get(getIndex());
                        handleStatusChange(appointment, AppointmentStatus.SCHEDULED);
                    });
                    
                    rejectBtn.setOnAction(event -> {
                        Appointment appointment = getTableView().getItems().get(getIndex());
                        handleStatusChange(appointment, AppointmentStatus.REJECTED);
                    });
                }
                
                @Override
                protected void updateItem(Appointment appointment, boolean empty) {
                    super.updateItem(appointment, empty);
                    
                    if (appointment == null || empty) {
                        setGraphic(null);
                    } else {
                        // Only show buttons for REQUESTED appointments
                        if (appointment.getStatus() == AppointmentStatus.REQUESTED) {
                            HBox buttonsBox = new HBox(5, acceptBtn, rejectBtn);
                            buttonsBox.setPadding(new Insets(0, 5, 0, 5));
                            setGraphic(buttonsBox);
                        } else {
                            setGraphic(null);
                        }
                    }
                }
            };
        });
    }
    
    /**
     * Set up filter options for the table
     */
    private void setupFilters() {
        // Set up status filter
        statusFilterComboBox.getItems().add("All Statuses");
        for (AppointmentStatus status : AppointmentStatus.values()) {
            statusFilterComboBox.getItems().add(status.toString());
        }
        statusFilterComboBox.setValue("All Statuses");
        
        // Create the filtered list
        filteredAppointments = new FilteredList<>(allAppointments);
        
        // Add listeners to update filter predicate when filters change
        statusFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        futureDateCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        
        // Set the filtered list as table items
        appointmentsTable.setItems(filteredAppointments);
    }
    
    /**
     * Apply filters based on UI controls
     */
    private void applyFilters() {
        filteredAppointments.setPredicate(createFilterPredicate());
    }
    
    /**
     * Create a filter predicate based on current filter settings
     */
    private Predicate<Appointment> createFilterPredicate() {
        // Status filter
        String statusFilter = statusFilterComboBox.getValue();
        boolean filterByStatus = !"All Statuses".equals(statusFilter);
        
        // Future appointments filter
        boolean showOnlyFuture = futureDateCheckBox.isSelected();
        
        // Search text
        String searchText = searchField.getText().toLowerCase().trim();
        boolean hasSearchText = !searchText.isEmpty();
        
        return appointment -> {
            // Status filter
            if (filterByStatus && !appointment.getStatus().toString().equals(statusFilter)) {
                return false;
            }
            
            // Future date filter
            if (showOnlyFuture) {
                Schedule schedule = appointment.getSchedule();
                if (schedule == null || schedule.getStartingTime() == null || 
                    schedule.getStartingTime().isBefore(LocalDateTime.now())) {
                    return false;
                }
            }
            
            // Search filter
            if (hasSearchText) {
                Patient patient = appointment.getPatient();
                String patientName = patient.getFirstName() + " " + patient.getLastName();
                return patientName.toLowerCase().contains(searchText);
            }
            
            return true;
        };
    }
    
    /**
     * Load appointments from the service
     */
    private void loadAppointments() {
        try {
            // Get appointments for the current doctor
            List<Appointment> appointments = appointmentService.getAllAppointments();
            
            if (appointments != null && !appointments.isEmpty()) {
                allAppointments.clear();
                allAppointments.addAll(appointments);
                
                // Apply initial filters
                applyFilters();
            } else {
                // No appointments
                showEmptyTableMessage();
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Data Loading Error", "Failed to load appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Show a message in the table when there are no appointments
     */
    private void showEmptyTableMessage() {
        Label placeholderLabel = new Label("No appointments found. Use the patient dashboard to see new appointment requests.");
        placeholderLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        appointmentsTable.setPlaceholder(placeholderLabel);
    }
    
    /**
     * Update the enabled/disabled state of buttons based on the selected appointment
     */
    private void updateButtonStates(Appointment appointment) {
        if (appointment == null) {
            acceptButton.setDisable(true);
            rejectButton.setDisable(true);
            completeButton.setDisable(true);
            rescheduleButton.setDisable(true);
            updateLinkButton.setDisable(true);
            return;
        }
        
        // Enable/disable buttons based on appointment status
        AppointmentStatus status = appointment.getStatus();
        
        // Accept button - only for REQUESTED status
        acceptButton.setDisable(status != AppointmentStatus.REQUESTED);
        
        // Reject button - for REQUESTED or SCHEDULED status
        rejectButton.setDisable(status != AppointmentStatus.REQUESTED && status != AppointmentStatus.SCHEDULED);
        
        // Complete button - only for SCHEDULED status
        completeButton.setDisable(status != AppointmentStatus.SCHEDULED);
        
        // Reschedule button - for SCHEDULED or POSTPONED status
        rescheduleButton.setDisable(status != AppointmentStatus.SCHEDULED && status != AppointmentStatus.POSTPONED);
        
        // Update link button - only for SCHEDULED status
        updateLinkButton.setDisable(status != AppointmentStatus.SCHEDULED);
    }
    
    /**
     * Show appointment details in the details pane
     */
    private void showAppointmentDetails(Appointment appointment) {
        if (appointment == null) {
            appointmentDetailsPane.setVisible(false);
            return;
        }
        
        // Show the details pane
        appointmentDetailsPane.setVisible(true);
        
        // Set patient details
        Patient patient = appointment.getPatient();
        detailsPatientLabel.setText(patient.getFirstName() + " " + patient.getLastName());
        
        // Set date and time
        Schedule schedule = appointment.getSchedule();
        if (schedule != null && schedule.getStartingTime() != null) {
            detailsDateTimeLabel.setText(
                schedule.getStartingTime().format(DATE_FORMATTER) + " at " +
                schedule.getStartingTime().format(TIME_FORMATTER) + " - " +
                schedule.getEndingTime().format(TIME_FORMATTER)
            );
        } else {
            detailsDateTimeLabel.setText("Not scheduled");
        }
        
        // Set status
        detailsStatusLabel.setText(appointment.getStatus().toString());
        
        // Set link
        String link = appointment.getLinkForRoom();
        linkTextField.setText(link != null ? link : "");
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
     * Handle changing the status of an appointment
     */
    private void handleStatusChange(Appointment appointment, AppointmentStatus newStatus) {
        try {
            // Update appointment status
            String result = appointmentService.changeAppointmentStatus(appointment.getId(), newStatus);
            
            if (result.contains("Successfully")) {
                // Update the appointment in our list
                appointment.setStatus(newStatus);
                
                // Refresh the table and detail view
                appointmentsTable.refresh();
                showAppointmentDetails(appointment);
                
                // Show success message
                showInfoAlert("Status Updated", "Appointment Status", 
                        "Appointment status has been updated to " + newStatus);
            } else {
                showErrorAlert("Error", "Status Update Failed", result);
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Status Update Error", "Failed to update appointment status: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Handle accept button click
     */
    @FXML
    private void handleAccept() {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            handleStatusChange(selected, AppointmentStatus.SCHEDULED);
        }
    }
    
    /**
     * Handle reject button click
     */
    @FXML
    private void handleReject() {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            handleStatusChange(selected, AppointmentStatus.REJECTED);
        }
    }
    
    /**
     * Handle complete button click
     */
    @FXML
    private void handleComplete() {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Mark appointment as COMPLETED (using CANCELED as proxy since it doesn't exist in enum)
            handleStatusChange(selected, AppointmentStatus.CANCELED);
        }
    }
    
    /**
     * Handle reschedule button click - open a dialog for rescheduling
     */
    @FXML
    private void handleReschedule() {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Mark as postponed and then open reschedule dialog would go here
            handleStatusChange(selected, AppointmentStatus.POSTPONED);
            
            // TODO: Open a dialog to select new date/time
            showInfoAlert("Reschedule", "Appointment Rescheduling", 
                    "The appointment has been marked as postponed. Please contact the patient to arrange a new time.");
        }
    }
    
    /**
     * Handle updating the meeting link for an appointment
     */
    @FXML
    private void handleUpdateLink() {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String link = linkTextField.getText().trim();
            
            try {
                // Use the new dedicated method to update the meeting link
                String result = appointmentService.addMeetingLink(selected.getId(), link);
                
                if (result.contains("successfully")) {
                    // Update the link in the local appointment object
                    selected.setLinkForRoom(link);
                    
                    // Refresh the table
                    appointmentsTable.refresh();
                    
                    showInfoAlert("Link Updated", "Meeting Link", 
                            "The meeting link has been updated successfully.");
                } else {
                    showErrorAlert("Error", "Link Update Error", result);
                }
            } catch (Exception e) {
                showErrorAlert("Error", "Link Update Error", "Failed to update meeting link: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Handle search button click
     */
    @FXML
    private void handleSearch() {
        applyFilters();
    }
    
    /**
     * Handle reset button click
     */
    @FXML
    private void handleReset() {
        // Reset filters
        statusFilterComboBox.setValue("All Statuses");
        searchField.clear();
        
        // Keep future date filter as is, as that's usually preferred
        
        // Apply filters
        applyFilters();
    }
    
    /**
     * Handle back button click
     */
    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.DOCTOR_DASHBOARD, ScreenPaths.TITLE_DOCTOR_DASHBOARD);
    }
}