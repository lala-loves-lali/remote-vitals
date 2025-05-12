package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.entities.Doctor;
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
import javafx.beans.property.SimpleStringProperty;

import java.net.URL;
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
    private TableColumn<Patient, Void> actionColumn;
    
    private ObservableList<Patient> patients = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
//        try {
//            // Get current doctor
//            Doctor currentDoctor = getDoctorUser();
//            if (currentDoctor == null) {
//                showErrorAlert("Error", "Session Error", "No doctor session found. Please log in again.");
//                return;
//            }
//
//            // Get assigned patients
//            patients.setAll(currentDoctor.getAssignedPatients());
//            patientsTable.setItems(patients);
//
//        } catch (Exception e) {
//            showErrorAlert("Error", "Loading Error", "Failed to load patients: " + e.getMessage());
//        }
    }
    
    private void handleCheckup(Patient patient) {
//        try {
//            // Store the selected patient in the controller
//            setCurrentUser(patient);
//            // Navigate to checkup screen
//            navigateTo(new ActionEvent(), ScreenPaths.DOCTOR_CHECKUP, ScreenPaths.TITLE_DOCTOR_CHECKUP);
//        } catch (Exception e) {
//            showErrorAlert("Error", "Navigation Error", "Failed to open checkup screen: " + e.getMessage());
//        }
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.DOCTOR_DASHBOARD, ScreenPaths.TITLE_DOCTOR_DASHBOARD);
    }
} 