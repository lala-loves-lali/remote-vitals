package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.checkup.entities.CheckUp;
import com.remote_vitals.backend.services.CheckUpService;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Callback;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the patient medical history screen.
 * Displays the patient's medical history including checkups.
 */
public class PatientMedicalHistoryController extends BaseController implements Initializable {

    @FXML
    private Label id_label;

    @FXML
    private Label name_label;

    @FXML
    private TableView<CheckUp> checkupsTable;

    @FXML
    private TableColumn<CheckUp, String> dateColumn;

    @FXML
    private TableColumn<CheckUp, String> doctorColumn;

    @FXML
    private TableColumn<CheckUp, String> prescriptionColumn;

    @FXML
    private TableColumn<CheckUp, String> feedbackColumn;

    @FXML
    private TableColumn<CheckUp, Void> actionColumn;

    private Patient currentPatient;
    private ObservableList<CheckUp> checkups = FXCollections.observableArrayList();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

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

            // Load checkups
            loadCheckups();
        } catch (Exception e) {
            showErrorAlert("Error", "Initialization Error", "Failed to initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set up the table columns with appropriate cell factories
     */
    private void setupTableColumns() {
        // Date column
        dateColumn.setCellValueFactory(cellData -> {
            CheckUp checkup = cellData.getValue();
            return new SimpleStringProperty(checkup.getTimeWhenMade().format(DATE_FORMAT));
        });

        // Doctor column
        doctorColumn.setCellValueFactory(cellData -> {
            CheckUp checkup = cellData.getValue();
            Doctor doctor = checkup.getDoctor();
            return new SimpleStringProperty(doctor.getFirstName() + " " + doctor.getLastName());
        });

        // Prescription column - with text wrapping and custom cell rendering
        prescriptionColumn.setCellValueFactory(cellData -> {
            CheckUp checkup = cellData.getValue();
            String prescription = checkup.getPrescription();
            if (prescription != null && prescription.length() > 40) {
                return new SimpleStringProperty(prescription.substring(0, 37) + "...");
            }
            return new SimpleStringProperty(prescription);
        });
        
        prescriptionColumn.setCellFactory(column -> {
            return new TableCell<CheckUp, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        setText(item);
                        
                        CheckUp checkup = getTableView().getItems().get(getIndex());
                        String fullPrescription = checkup.getPrescription();
                        
                        if (fullPrescription != null && fullPrescription.length() > 40) {
                            Tooltip tooltip = new Tooltip(fullPrescription);
                            tooltip.setWrapText(true);
                            tooltip.setMaxWidth(400);
                            setTooltip(tooltip);
                        }
                        
                        // Set style for larger row height
                        setStyle("-fx-padding: 10 5 10 5;");
                    }
                }
            };
        });

        // Feedback column - with text wrapping and custom cell rendering
        feedbackColumn.setCellValueFactory(cellData -> {
            CheckUp checkup = cellData.getValue();
            String feedback = checkup.getFeedback();
            if (feedback != null && feedback.length() > 40) {
                return new SimpleStringProperty(feedback.substring(0, 37) + "...");
            }
            return new SimpleStringProperty(feedback);
        });
        
        feedbackColumn.setCellFactory(column -> {
            return new TableCell<CheckUp, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        setText(item);
                        
                        CheckUp checkup = getTableView().getItems().get(getIndex());
                        String fullFeedback = checkup.getFeedback();
                        
                        if (fullFeedback != null && fullFeedback.length() > 40) {
                            Tooltip tooltip = new Tooltip(fullFeedback);
                            tooltip.setWrapText(true);
                            tooltip.setMaxWidth(400);
                            setTooltip(tooltip);
                        }
                        
                        // Set style for larger row height
                        setStyle("-fx-padding: 10 5 10 5;");
                    }
                }
            };
        });

        // Action column - View details button
        addViewDetailsButtonToTable();
        
        // Set row height
        checkupsTable.setRowFactory(tv -> {
            TableRow<CheckUp> row = new TableRow<>();
            row.setPrefHeight(60); // Increased row height
            return row;
        });
    }

    /**
     * Load checkups from the database
     */
    private void loadCheckups() {
        try {
            if (getContext() != null) {
                CheckUpService checkUpService = getContext().getBean(CheckUpService.class);
                List<CheckUp> patientCheckups = checkUpService.getAllCheckUpsOf(currentPatient.getId(), Patient.class);
                
                if (patientCheckups != null && !patientCheckups.isEmpty()) {
                    checkups.clear();
                    checkups.addAll(patientCheckups);
                    checkupsTable.setItems(checkups);
                } else {
                    // Show a placeholder message if no checkups
                    Label placeholder = new Label("No medical history records found");
                    placeholder.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
                    checkupsTable.setPlaceholder(placeholder);
                }
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Data Loading Error", "Failed to load medical history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Add a "View Details" button to each row in the table
     */
    private void addViewDetailsButtonToTable() {
        Callback<TableColumn<CheckUp, Void>, TableCell<CheckUp, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<CheckUp, Void> call(final TableColumn<CheckUp, Void> param) {
                return new TableCell<>() {
                    private final Button viewButton = new Button("View");
                    {
                        viewButton.setOnAction((ActionEvent event) -> {
                            CheckUp checkup = getTableView().getItems().get(getIndex());
                            showCheckupDetailsDialog(checkup);
                        });
                        
                        // Style the button
                        viewButton.getStyleClass().add("action-button");
                        viewButton.setStyle("-fx-background-color: #0066cc; -fx-text-fill: white;");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(viewButton);
                        }
                    }
                };
            }
        };
        
        actionColumn.setCellFactory(cellFactory);
    }

    /**
     * Show a detailed view of a checkup in a dialog
     * 
     * @param checkup The checkup to display
     */
    private void showCheckupDetailsDialog(CheckUp checkup) {
        // Create dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Checkup Details");
        dialog.setHeaderText("Checkup from " + checkup.getTimeWhenMade().format(DATE_FORMAT));
        
        // Set button types
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        // Create layout
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        // Doctor info
        Label doctorLabel = new Label("Doctor: " + checkup.getDoctor().getFirstName() + " " + checkup.getDoctor().getLastName());
        doctorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        // Prescription section
        Label prescriptionTitle = new Label("Prescription:");
        prescriptionTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        TextArea prescriptionArea = new TextArea(checkup.getPrescription());
        prescriptionArea.setWrapText(true);
        prescriptionArea.setEditable(false);
        prescriptionArea.setPrefWidth(550);
        prescriptionArea.setPrefHeight(150);
        prescriptionArea.setStyle("-fx-font-size: 14px;");
        
        // Feedback section
        Label feedbackTitle = new Label("Doctor's Feedback:");
        feedbackTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        TextArea feedbackArea = new TextArea(checkup.getFeedback());
        feedbackArea.setWrapText(true);
        feedbackArea.setEditable(false);
        feedbackArea.setPrefWidth(550);
        feedbackArea.setPrefHeight(150);
        feedbackArea.setStyle("-fx-font-size: 14px;");
        
        // Add elements to content
        content.getChildren().addAll(
            doctorLabel,
            new Separator(),
            prescriptionTitle,
            prescriptionArea,
            new Separator(),
            feedbackTitle,
            feedbackArea
        );
        
        // Set content
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setPrefWidth(600);
        
        // Show dialog
        dialog.showAndWait();
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
     * Handler for the Appointments button
     */
    @FXML
    private void handleAppointments(ActionEvent event) {
        navigateTo(event, ScreenPaths.PATIENT_APPOINTMENTS, ScreenPaths.TITLE_PATIENT_APPOINTMENTS);
    }

    /**
     * Handler for the Logout button
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        navigateTo(event, ScreenPaths.LOGIN, ScreenPaths.TITLE_LOGIN);
    }
} 