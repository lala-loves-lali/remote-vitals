package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.checkup.entities.CheckUp;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.vitalReport.entities.VitalReport;
import com.remote_vitals.backend.vital.entities.VitalRecord;
import com.remote_vitals.backend.vital.entities.HeartRate;
import com.remote_vitals.backend.vital.entities.BloodPressureSystolic;
import com.remote_vitals.backend.vital.entities.BloodPressureDiastolic;
import com.remote_vitals.backend.vital.entities.BodyTemperature;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DoctorCheckupController extends BaseController implements Initializable {

    @FXML
    private Label patientNameLabel;
    
    @FXML
    private Label emailLabel;
    
    @FXML
    private Label phoneLabel;
    
    @FXML
    private Label bloodGroupLabel;
    
    @FXML
    private TableView<VitalReport> vitalsTable;
    
    @FXML
    private TableColumn<VitalReport, String> dateColumn;
    
    @FXML
    private TableColumn<VitalReport, String> heartRateColumn;
    
    @FXML
    private TableColumn<VitalReport, String> bloodPressureColumn;
    
    @FXML
    private TableColumn<VitalReport, String> temperatureColumn;
    
    @FXML
    private TextArea prescriptionArea;
    
    @FXML
    private TextArea feedbackArea;
    
    private Patient currentPatient;
    private ObservableList<VitalReport> vitals = FXCollections.observableArrayList();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        // Get the patient from the previous screen
//        currentPatient = (Patient) getCurrentUser();
//        if (currentPatient == null) {
//            showErrorAlert("Error", "Session Error", "No patient selected. Please try again.");
//            return;
//        }
//
//        // Set up patient information
//        setupPatientInfo();
//
//        // Set up vitals table
//        setupVitalsTable();
//
//        // Load vitals
//        loadVitals();
//    }
//
//    private void setupPatientInfo() {
//        patientNameLabel.setText(currentPatient.getFirstName() + " " + currentPatient.getLastName());
//        emailLabel.setText(currentPatient.getEmail());
//        phoneLabel.setText(currentPatient.getPhoneNumber());
//        bloodGroupLabel.setText(currentPatient.getBloodGroup());
//    }
//
//    private void setupVitalsTable() {
//        dateColumn.setCellValueFactory(cellData -> {
//            VitalReport report = cellData.getValue();
//            return new SimpleStringProperty(
//                report.getReportWhenMade().format(DATE_FORMATTER)
//            );
//        });
//
//        heartRateColumn.setCellValueFactory(cellData -> {
//            VitalReport report = cellData.getValue();
//            float heartRate = report.getVitalRecords().stream()
//                .filter(record -> record instanceof HeartRate)
//                .findFirst()
//                .map(VitalRecord::getValue)
//                .orElse(0f);
//            return new SimpleStringProperty(heartRate + " bpm");
//        });
//
//        bloodPressureColumn.setCellValueFactory(cellData -> {
//            VitalReport report = cellData.getValue();
//            float systolic = report.getVitalRecords().stream()
//                .filter(record -> record instanceof BloodPressureSystolic)
//                .findFirst()
//                .map(VitalRecord::getValue)
//                .orElse(0f);
//            float diastolic = report.getVitalRecords().stream()
//                .filter(record -> record instanceof BloodPressureDiastolic)
//                .findFirst()
//                .map(VitalRecord::getValue)
//                .orElse(0f);
//            return new SimpleStringProperty(systolic + "/" + diastolic + " mmHg");
//        });
//
//        temperatureColumn.setCellValueFactory(cellData -> {
//            VitalReport report = cellData.getValue();
//            float temp = report.getVitalRecords().stream()
//                .filter(record -> record instanceof BodyTemperature)
//                .findFirst()
//                .map(VitalRecord::getValue)
//                .orElse(0f);
//            return new SimpleStringProperty(temp + " °C");
//        });
    }
    
    private void loadVitals() {
//        try {
//            // Get the latest vitals for the patient
//            vitals.setAll(currentPatient.getVitalReport());
//            vitalsTable.setItems(vitals);
//        } catch (Exception e) {
//            showErrorAlert("Error", "Loading Error", "Failed to load vitals: " + e.getMessage());
//        }
    }
    
    @FXML
    private void handleSave(ActionEvent event) {
//        try {
//            String prescription = prescriptionArea.getText().trim();
//            String feedback = feedbackArea.getText().trim();
//
//            if (prescription.isEmpty() || feedback.isEmpty()) {
//                showErrorAlert("Error", "Missing Information",
//                    "Please fill in both prescription and feedback fields.");
//                return;
//            }
//
//            // Create and save checkup
//            CheckUp checkup = new CheckUp();
//            checkup.setPatient(currentPatient);
//            checkup.setDoctor(getDoctorUser());
//            checkup.setPrescription(prescription);
//            checkup.setFeedback(feedback);
//            checkup.setTimeWhenMade(LocalDateTime.now());
//
//            // Save to database
//            getDb().addCheckUp(checkup);
//
//            showInfoAlert("Success", "Checkup Saved",
//                "The checkup details have been saved successfully.");
//
//            // Navigate back to patients list
//            navigateTo(event, ScreenPaths.DOCTOR_PATIENTS, ScreenPaths.TITLE_DOCTOR_PATIENTS);
//
//        } catch (Exception e) {
//            showErrorAlert("Error", "Save Error",
//                "Failed to save checkup: " + e.getMessage());
//        }
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.DOCTOR_PATIENTS, ScreenPaths.TITLE_DOCTOR_PATIENTS);
    }
} 