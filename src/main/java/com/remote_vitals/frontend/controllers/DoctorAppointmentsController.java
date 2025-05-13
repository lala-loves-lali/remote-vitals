package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorAppointmentsController extends BaseController {
    
    @FXML
    private TableView<Appointment> appointmentsTable;
    
    @FXML
    private TableColumn<Appointment, String> patientNameColumn;
    
    @FXML
    private TableColumn<Appointment, LocalDate> dateColumn;
    
    @FXML
    private TableColumn<Appointment, String> timeColumn;
    
    @FXML
    private TableColumn<Appointment, String> reasonColumn;
    
    @FXML
    private TableColumn<Appointment, String> statusColumn;
    
    @FXML
    private Button acceptButton;
    
    @FXML
    private Button rejectButton;
    
    @FXML
    private Button backButton;
    
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    
    @FXML
    private void initialize() {
        // Initialize table columns
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Set up the table
        appointmentsTable.setItems(appointments);
        
        // Add sample data (replace with actual database data)
        loadAppointments();
        
        // Disable accept/reject buttons initially
        acceptButton.setDisable(true);
        rejectButton.setDisable(true);
        
        // Add selection listener
        appointmentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            acceptButton.setDisable(!hasSelection || !"PENDING".equals(newSelection.getStatus()));
            rejectButton.setDisable(!hasSelection || !"PENDING".equals(newSelection.getStatus()));
        });
    }
    
    private void loadAppointments() {
        // TODO: Replace with actual database calls
        appointments.add(new Appointment("John Doe", LocalDate.now().plusDays(1), "10:00 AM", "Regular checkup", "PENDING"));
        appointments.add(new Appointment("Jane Smith", LocalDate.now().plusDays(2), "2:30 PM", "Follow-up consultation", "PENDING"));
    }
    
    @FXML
    private void handleAccept(ActionEvent event) {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            selectedAppointment.setStatus("ACCEPTED");
            appointmentsTable.refresh();
            showInfoAlert("Appointment Accepted", "Success", 
                    "Appointment with " + selectedAppointment.getPatientName() + " has been accepted.");
        }
    }
    
    @FXML
    private void handleReject(ActionEvent event) {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            selectedAppointment.setStatus("REJECTED");
            appointmentsTable.refresh();
            showInfoAlert("Appointment Rejected", "Success", 
                    "Appointment with " + selectedAppointment.getPatientName() + " has been rejected.");
        }
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.DOCTOR_DASHBOARD, ScreenPaths.TITLE_DOCTOR_DASHBOARD);
    }
}