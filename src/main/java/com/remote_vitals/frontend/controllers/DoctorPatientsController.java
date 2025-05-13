package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.services.CheckUpService;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.User;
import com.remote_vitals.backend.checkup.entities.CheckUp;
import com.remote_vitals.backend.services.PatientService;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.beans.property.SimpleStringProperty;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class DoctorPatientsController extends BaseController implements Initializable {

    @FXML
    private TableView<Patient> patientsTable;
    
    @FXML
    private TableColumn<Patient, String> nameColumn;
    
    @FXML
    private TableColumn<Patient, String> emailColumn;
    
    @FXML
    private TableColumn<Patient, String> phoneColumn;
    
    @FXML
    private TableColumn<Patient, String> bloodGroupColumn;
    
    @FXML
    private TableColumn<Patient, String> dobColumn;
    
    @FXML
    private TableColumn<Patient, String> medicalHistoryColumn;
    
    @FXML
    private TableColumn<Patient, Void> actionColumn;
    
    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    
    // Static patient reference that will be used by the checkup screen
    private static Patient selectedPatient;
    
    // Simple cache for data that needs to be passed between screens
    private static final Map<String, Object> tempDataMap = new HashMap<>();
    
    // Patient service for retrieving patient data
    private PatientService patientService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the patient service from the application context
        if (getContext() != null) {
            patientService = getContext().getBean(PatientService.class);
        }
        
        // Set up table columns
        nameColumn.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            return new SimpleStringProperty(
                patient.getFirstName() + " " + patient.getLastName()
            );
        });
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        bloodGroupColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        
        // Set up date of birth column with formatting
        dobColumn.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            LocalDate dob = patient.getDateOfBirth();
            if (dob != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                return new SimpleStringProperty(dob.format(formatter));
            } else {
                return new SimpleStringProperty("N/A");
            }
        });
        
        // Set up medical history column with truncated text if too long
        medicalHistoryColumn.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            String history = patient.getMedicalHistory();
            if (history != null && history.length() > 50) {
                return new SimpleStringProperty(history.substring(0, 47) + "...");
            }
            return new SimpleStringProperty(history != null ? history : "No history");
        });
        
        // Add tooltip for medical history to show full text on hover
        medicalHistoryColumn.setCellFactory(column -> {
            return new TableCell<Patient, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (item == null || empty) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        setText(item);
                        
                        Patient patient = getTableView().getItems().get(getIndex());
                        String fullHistory = patient.getMedicalHistory();
                        
                        if (fullHistory != null && fullHistory.length() > 50) {
                            Tooltip tooltip = new Tooltip(fullHistory);
                            tooltip.setWrapText(true);
                            tooltip.setMaxWidth(400);
                            setTooltip(tooltip);
                        }
                    }
                }
            };
        });
        
        // Add checkup button to each row
        addCheckupButtonToTable();
        
        // Load patients
        loadPatients();
    }
    
    private void addCheckupButtonToTable() {
        Callback<TableColumn<Patient, Void>, TableCell<Patient, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Patient, Void> call(final TableColumn<Patient, Void> param) {
                return new TableCell<>() {
                    private final Button checkupButton = new Button("Checkup");
                    
                    {
                        checkupButton.setOnAction((ActionEvent event) -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            handleCheckup(patient);
                        });
                        
                        // Style the button
                        checkupButton.getStyleClass().add("action-button");
                        checkupButton.setStyle("-fx-background-color: #0066cc; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                        checkupButton.setPrefWidth(80);
                        checkupButton.setMaxWidth(Double.MAX_VALUE);
                        checkupButton.setPadding(new Insets(5, 10, 5, 10));
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(checkupButton);
                        }
                    }
                };
            }
        };
        
        actionColumn.setCellFactory(cellFactory);
    }
    
    private void loadPatients() {
        try {
            List<Patient> patientList;

            // Check if patient service is available
            if (patientService != null) {
                // Get patients assigned to the current doctor
                patientList = patientService.getAllPatients();
            } else {
                // Fallback to manual data if service is not available
                Optional<User> currentUserOptional = getCurrentUser();
                if (!currentUserOptional.isPresent() || !(currentUserOptional.get() instanceof Doctor)) {
                    showErrorAlert("Error", "Session Error", "No doctor session found. Please log in again.");
                    return;
                }

                Doctor currentDoctor = (Doctor) currentUserOptional.get();

                // Create some dummy patients for demonstration (fallback)
                Patient patient1 = Patient.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("john.doe@example.com")
                    .phoneNumber("555-1234")
                    .bloodGroup("O+")
                    .dateOfBirth(LocalDate.of(1985, 5, 15))
                    .medicalHistory("Patient has a history of hypertension and type 2 diabetes. Regular medication includes Metformin and Lisinopril.")
                    .build();

                Patient patient2 = Patient.builder()
                    .firstName("Jane")
                    .lastName("Smith")
                    .email("jane.smith@example.com")
                    .phoneNumber("555-5678")
                    .bloodGroup("A-")
                    .dateOfBirth(LocalDate.of(1990, 8, 21))
                    .medicalHistory("Allergic to penicillin. Has asthma and uses an inhaler as needed.")
                    .build();

                Patient patient3 = Patient.builder()
                    .firstName("Robert")
                    .lastName("Johnson")
                    .email("robert.j@example.com")
                    .phoneNumber("555-9012")
                    .bloodGroup("B+")
                    .dateOfBirth(LocalDate.of(1978, 3, 10))
                    .medicalHistory("Had appendectomy in 2015. No chronic conditions.")
                    .build();

                patientList = List.of(patient1, patient2, patient3);
                System.out.println("WARNING: Using fallback patient data because PatientService is not available");
            }

            // Clear and add all patients to the observable list
            patients.clear();
            patients.addAll(patientList);

            // Set the items to the table
            patientsTable.setItems(patients);

        } catch (Exception e) {
            showErrorAlert("Error", "Loading Error", "Failed to load patients: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Store temporary data for cross-controller communication
     */
    private void storeTempData(String key, Object value) {
        tempDataMap.put(key, value);
    }
    
    /**
     * Retrieve temporary data for cross-controller communication
     */
    @SuppressWarnings("unchecked")
    private <T> T getTempData(String key) {
        return (T) tempDataMap.get(key);
    }
    
    /**
     * Set the selected patient to be accessed by other controllers
     */
    public static void setSelectedPatient(Patient patient) {
        selectedPatient = patient;
    }
    
    /**
     * Get the selected patient from other controllers
     */
    public static Patient getSelectedPatient() {
        return selectedPatient;
    }
    
    private void createCheckup(Patient patient) {
        try {
            // Get the current doctor
            Doctor doctor = getDoctorUser();
            if (doctor == null) {
                throw new Exception("No doctor session found");
            }
            
            // For now, just create a checkup with placeholder data
            CheckUp checkup = CheckUp.builder()
                .patient(patient)
                .doctor(doctor)
                .timeWhenMade(LocalDateTime.now())
                .prescription("Pending doctor's prescription")
                .feedback("Pending doctor's feedback")
                .build();
                
            // In a real app, this would be saved to the database
            // getContext().getBean(CheckupRepository.class).save(checkup);
            
            // For demo purposes, just log info about the checkup
            System.out.println("Created checkup: " + checkup);
        } catch (Exception e) {
            showErrorAlert("Error", "Checkup Creation Error", "Failed to create checkup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleCheckup(Patient patient) {
        try {
            // Store the selected patient for the checkup screen
            setSelectedPatient(patient);
            
            // Create the checkup entry
            createCheckup(patient);
            
            // Create a dialog for recording a checkup with prescription and feedback
            Dialog<CheckUp> dialog = new Dialog<>();
            dialog.setTitle("Patient Checkup");
            dialog.setHeaderText("Create Checkup for " + patient.getFirstName() + " " + patient.getLastName());
            
            // Set the button types
            ButtonType saveButtonType = new ButtonType("Save Checkup", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
            
            // Create the prescription and feedback fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            
            TextArea prescription = new TextArea();
            prescription.setPromptText("Enter prescription details");
            prescription.setPrefRowCount(5);
            
            TextArea feedback = new TextArea();
            feedback.setPromptText("Enter feedback for the patient");
            feedback.setPrefRowCount(5);
            
            grid.add(new Label("Patient Information:"), 0, 0);
            grid.add(new Label(String.format(
                "Name: %s %s\nEmail: %s\nPhone: %s\nBlood Group: %s\nMedical History: %s",
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getPhoneNumber(),
                patient.getBloodGroup(),
                patient.getMedicalHistory() != null ? 
                    (patient.getMedicalHistory().length() > 100 ? 
                        patient.getMedicalHistory().substring(0, 97) + "..." : 
                        patient.getMedicalHistory()) : 
                    "No history"
            )), 1, 0);
            
            grid.add(new Label("Prescription:"), 0, 1);
            grid.add(prescription, 1, 1);
            grid.add(new Label("Feedback:"), 0, 2);
            grid.add(feedback, 1, 2);
            
            dialog.getDialogPane().setContent(grid);
            
            // Request focus on the prescription field by default
            prescription.requestFocus();
            
            // Convert the result to a prescription-feedback pair when the save button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    // Get the current doctor
                    Doctor doctor = getDoctorUser();
                    if (doctor != null) {
                        return CheckUp.builder()
                            .patient(patient)
                            .doctor(doctor)
                            .timeWhenMade(LocalDateTime.now())
                            .prescription(prescription.getText())
                            .feedback(feedback.getText())
                            .build();
                    }
                }
                return null;
            });


            
            // Show the dialog and process the result
            Optional<CheckUp> result = dialog.showAndWait();
            result.ifPresent(checkup -> {

                System.out.println("Saving checkup");
                getContext().getBean(CheckUpService.class).submitCheckUp(patient.getId(), getDoctorUser().getId(), prescription.getText(), feedback.getText());

                showInfoAlert("Success", "Checkup Created",
                    "Checkup was successfully created for " + patient.getFirstName() + " " + patient.getLastName());
            });
            
            // Navigate to the doctor checkup screen (commented out for now as we're using dialogs)
            // navigateTo(new ActionEvent(), ScreenPaths.DOCTOR_CHECKUP, ScreenPaths.TITLE_DOCTOR_CHECKUP);
        } catch (Exception e) {
            showErrorAlert("Error", "Checkup Error", "Failed to process checkup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.DOCTOR_DASHBOARD, ScreenPaths.TITLE_DOCTOR_DASHBOARD);
    }
} 