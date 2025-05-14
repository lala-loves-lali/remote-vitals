package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.vital.entities.*;
import com.remote_vitals.backend.vitalReport.entities.VitalReport;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.application.Platform;
import java.util.Arrays;
import java.net.URL;
import java.util.ResourceBundle;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Controller for the vitals visualization screen.
 * Displays graphs of patient vital data over time.
 */
public class VitalsGraphController extends BaseController implements Initializable {

    @FXML
    private LineChart<Number, Number> vitalsChart;
    
    @FXML
    private NumberAxis xAxis;
    
    @FXML
    private NumberAxis yAxis;
    
    @FXML
    private ChoiceBox<String> vitalTypeComboBox;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Button showAllButton;
    
    @FXML
    private Label patientNameLabel;
    
    @FXML
    private Label patientIdLabel;

    // Arrays to store vital types and their normal ranges
    private final String[] vitalTypes = {
        "Heart Rate (BPM)",
        "Blood Pressure - Systolic (mmHg)",
        "Blood Pressure - Diastolic (mmHg)",
        "Temperature (°C)",
        "Respiratory Rate (breaths/min)",
        "Oxygen Saturation (%)"
    };
    
    // Parallel array for normal ranges [min, max]
    private final double[][] vitalRanges = {
        {60, 100},              // Heart Rate
        {90, 120},              // Blood Pressure - Systolic
        {60, 80},               // Blood Pressure - Diastolic
        {36.1, 37.2},           // Temperature
        {12, 20},               // Respiratory Rate
        {95, 100}               // Oxygen Saturation
    };
    
    private Patient currentPatient;
    private List<VitalReport> patientVitalReports = new ArrayList<>();
    private Map<String, List<VitalRecord>> vitalRecordsByType = new HashMap<>();
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        
        System.out.println("********************************************");
        System.out.println("VitalsGraphController: initialize() started");
        System.out.println("********************************************");

        try {
            // Get current patient
            currentPatient = getPatientUser();
            System.out.println("Current patient: " + (currentPatient != null ? 
                currentPatient.getFirstName() + " " + currentPatient.getLastName() : "null"));
            
            if (currentPatient == null) {
                System.out.println("ERROR: No patient session found");
                showErrorAlert("Error", "Session Error", "No patient session found. Please log in again.");
                return;
            }
            
            // Set patient info
            patientNameLabel.setText(currentPatient.getFirstName() + " " + currentPatient.getLastName());
            patientIdLabel.setText("ID: P" + currentPatient.getId());
            System.out.println("Patient info set in UI");
            
            // Initialize collections to avoid NPE
            patientVitalReports = new ArrayList<>();
            vitalRecordsByType = new HashMap<>();
            System.out.println("Collections initialized");
            
            // Load vital reports from the patient
            System.out.println("Loading vital reports...");
            loadVitalReports();
            
            // Initialize the vital type dropdown
            System.out.println("Setting up vital type dropdown...");
            initializeVitalTypeDropdown();
            
            // Configure chart
            System.out.println("Configuring chart...");
            configureChart();
            
            // Initial data load
            System.out.println("Initial chart update...");
            updateChart();
            
            System.out.println("********************************************");
            System.out.println("VitalsGraphController initialization complete");
            System.out.println("********************************************");
        } catch (Exception e) {
            System.out.println("********************************************");
            System.out.println("ERROR during initialization: " + e.getMessage());
            e.printStackTrace();
            System.out.println("********************************************");
            showErrorAlert("Error", "Initialization Error", "Failed to initialize: " + e.getMessage());
        }
    }
    
    /**
     * Initialize the vital type dropdown with options and listener
     * Follows exact same pattern as LoginController
     */
    private void initializeVitalTypeDropdown() {
        System.out.println("============================================");
        System.out.println("initializeVitalTypeDropdown() started");
        
        // Check if the dropdown is available
        if (vitalTypeComboBox == null) {
            System.err.println("ERROR: vital type dropdown is null");
            return;
        }
        
        System.out.println("vitalTypeComboBox exists: " + vitalTypeComboBox);
        
        try {
            // Clear any existing items 
            vitalTypeComboBox.getItems().clear();
            System.out.println("Cleared existing items from dropdown");
            
            // Add all vital types to the dropdown
            for (String vitalType : vitalTypes) {
                vitalTypeComboBox.getItems().add(vitalType);
                System.out.println("Added item to dropdown: " + vitalType);
            }
            
            // Set default value to first item
            vitalTypeComboBox.setValue("Heart Rate (BPM)");
            System.out.println("Default value set to: Heart Rate (BPM)");
            
            // Add listener to update the chart when selection changes
            vitalTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                System.out.println("DROPDOWN SELECTION CHANGED: from " + oldVal + " to " + newVal);
                if (newVal != null) {
                    System.out.println("Updating y-axis and chart for: " + newVal);
                    updateYAxisForVitalType(newVal);
                    updateChart();
                }
            });
            
            // Add debug logging events
            vitalTypeComboBox.setOnShowing(event -> {
                System.out.println("EVENT: ChoiceBox dropdown is showing");
            });
            
            vitalTypeComboBox.setOnShown(event -> {
                System.out.println("EVENT: ChoiceBox dropdown is shown");
            });
            
            vitalTypeComboBox.setOnHidden(event -> {
                System.out.println("EVENT: ChoiceBox dropdown is hidden");
            });
            
            System.out.println("Dropdown setup complete with " + vitalTypeComboBox.getItems().size() + " items");
            System.out.println("Current selected value: " + vitalTypeComboBox.getValue());
            System.out.println("============================================");
        } catch (Exception e) {
            System.err.println("********************************************");
            System.err.println("ERROR setting up vital type dropdown: " + e.getMessage());
            e.printStackTrace();
            System.err.println("********************************************");
        }
    }
    
    /**
     * Loads vital reports from the patient and organizes them by type
     */
    private void loadVitalReports() {
        // Initialize collections to avoid NPE
        if (vitalRecordsByType == null) {
            vitalRecordsByType = new HashMap<>();
        } else {
            vitalRecordsByType.clear();
        }
        
        if (patientVitalReports == null) {
            patientVitalReports = new ArrayList<>();
        } else {
            patientVitalReports.clear();
        }
        
        if (currentPatient == null) {
            System.out.println("Current patient is null");
            return;
        }
        
        if (currentPatient.getVitalReports() == null || currentPatient.getVitalReports().isEmpty()) {
            System.out.println("No vital reports found for patient");
            return;
        }
        
        // Load reports from patient
        patientVitalReports = new ArrayList<>(currentPatient.getVitalReports());
        System.out.println("Loaded " + patientVitalReports.size() + " vital reports for patient " + currentPatient.getFirstName());
        
        // Sort reports by date (newest first)
        patientVitalReports.sort((r1, r2) -> r2.getReportWhenMade().compareTo(r1.getReportWhenMade()));
        
        // Organize vital records by type
        for (VitalReport report : patientVitalReports) {
            if (report.getVitalRecords() != null) {
                for (VitalRecord record : report.getVitalRecords()) {
                    String recordType = getVitalRecordType(record);
                    if (!vitalRecordsByType.containsKey(recordType)) {
                        vitalRecordsByType.put(recordType, new ArrayList<>());
                    }
                    vitalRecordsByType.get(recordType).add(record);
                }
            }
        }
        
        // Sort each type's records by date (oldest first for chart display)
        for (List<VitalRecord> records : vitalRecordsByType.values()) {
            records.sort(Comparator.comparing(record -> record.getVitalReport().getReportWhenMade()));
        }
        
        System.out.println("Organized vital records by type: " + vitalRecordsByType.keySet());
    }
    
    /**
     * Gets the type name of a vital record
     * 
     * @param record The VitalRecord object
     * @return The type name as a string
     */
    private String getVitalRecordType(VitalRecord record) {
        if (record instanceof HeartRate) return "Heart Rate (BPM)";
        if (record instanceof BloodPressureSystolic) return "Blood Pressure - Systolic (mmHg)";
        if (record instanceof BloodPressureDiastolic) return "Blood Pressure - Diastolic (mmHg)";
        if (record instanceof BodyTemperature) return "Temperature (°C)";
        if (record instanceof RespiratoryRate) return "Respiratory Rate (breaths/min)";
        if (record instanceof Weight) return "Weight (kg)";
        if (record instanceof Height) return "Height (cm)";
        if (record instanceof Haemoglobin) return "Haemoglobin (g/dL)";
        if (record instanceof RBC) return "Red Blood Cells (million/mm³)";
        if (record instanceof WBC) return "White Blood Cells (thousand/mm³)";
        if (record instanceof PlateletCount) return "Platelet Count (thousand/mm³)";
        if (record instanceof BloodVolume) return "Blood Volume (L)";
        return "Unknown Vital Type";
    }
    
    /**
     * Gets the index of a vital type in the array
     * 
     * @param vitalType The vital type to find
     * @return The index in the array, or -1 if not found
     */
    private int getVitalTypeIndex(String vitalType) {
        for (int i = 0; i < vitalTypes.length; i++) {
            if (vitalTypes[i].equals(vitalType)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Gets the normal range for a vital type
     * 
     * @param vitalType The vital type
     * @return The normal range as [min, max], or null if not found
     */
    private double[] getRangeForVitalType(String vitalType) {
        int index = getVitalTypeIndex(vitalType);
        if (index >= 0) {
            return vitalRanges[index];
        }
        return null;
    }
    
    /**
     * Configures the chart axes and labels.
     */
    private void configureChart() {
        // Configure chart appearance
        vitalsChart.setTitle("Vital Signs Over Time");
        vitalsChart.setCreateSymbols(true);
        vitalsChart.setAnimated(false);
        
        // Configure x-axis
        xAxis.setLabel("Day");
        xAxis.setTickMarkVisible(true);
        
        // Configure y-axis based on selected vital type
        String currentVitalType = vitalTypeComboBox.getValue();
        
        // Check if vital type is null and try to recover
        if (currentVitalType == null) {
            System.out.println("Warning: No vital type selected during chart configuration");
            
            // Try to set a default value if we have items
            if (vitalTypeComboBox != null && !vitalTypeComboBox.getItems().isEmpty()) {
                currentVitalType = vitalTypeComboBox.getItems().get(0);
                vitalTypeComboBox.setValue(currentVitalType);
                System.out.println("Set default vital type to: " + currentVitalType);
            } else {
                // If no items available, use a default label
                yAxis.setLabel("Value");
                yAxis.setAutoRanging(true);
                return;
            }
        }
        
        updateYAxisForVitalType(currentVitalType);
    }
    
    /**
     * Updates the y-axis scale and label based on the selected vital type.
     * 
     * @param vitalType The selected vital type
     */
    private void updateYAxisForVitalType(String vitalType) {
        if (vitalType == null) {
            System.out.println("Warning: Attempted to update y-axis with null vital type");
            yAxis.setLabel("Value");
            yAxis.setAutoRanging(true);
            return;
        }
        
        yAxis.setLabel(vitalType);
        
        // Set y-axis range based on vital type
        double[] range = getRangeForVitalType(vitalType);
        if (range != null) {
            // Add some padding to the min/max
            double padding = (range[1] - range[0]) * 0.2;
            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(Math.max(0, range[0] - padding));
            yAxis.setUpperBound(range[1] + padding);
            yAxis.setTickUnit((range[1] - range[0]) / 5);
        } else {
            yAxis.setAutoRanging(true);
        }
    }
    
    /**
     * Updates the chart with data based on the selected vital type.
     */
    private void updateChart() {
        System.out.println("============================================");
        System.out.println("updateChart() started");
        
        if (vitalsChart == null) {
            System.err.println("ERROR: Chart is null");
            return;
        }
        
        // Get the selected vital type
        String vitalType = vitalTypeComboBox != null ? vitalTypeComboBox.getValue() : null;
        
        System.out.println("Current vitalType from dropdown: " + vitalType);
        System.out.println("vitalTypeComboBox exists: " + (vitalTypeComboBox != null));
        if (vitalTypeComboBox != null) {
            System.out.println("vitalTypeComboBox items: " + vitalTypeComboBox.getItems().size());
        }
        
        // If no vital type is selected, try to set a default
        if (vitalType == null) {
            System.out.println("WARNING: No vital type selected");
            
            if (vitalTypeComboBox != null && !vitalTypeComboBox.getItems().isEmpty()) {
                vitalType = vitalTypeComboBox.getItems().get(0);
                System.out.println("Setting default vital type to: " + vitalType);
                vitalTypeComboBox.setValue(vitalType);
                System.out.println("Dropdown value is now: " + vitalTypeComboBox.getValue());
            } else if (vitalTypes.length > 0) {
                vitalType = vitalTypes[0];
                System.out.println("Using fallback vital type from array: " + vitalType);
            } else {
                System.out.println("ERROR: No vital types available");
                showErrorAlert("Error", "Chart Error", "No vital types available to display");
                return;
            }
        }
        
        // Final vitalType for use in lambdas
        final String finalVitalType = vitalType;
        System.out.println("Final vital type for chart: " + finalVitalType);
        
        // Clear previous data
        System.out.println("Clearing previous chart data");
        vitalsChart.getData().clear();
        
        // Update y-axis for the selected vital type
        System.out.println("Updating y-axis for: " + finalVitalType);
        updateYAxisForVitalType(finalVitalType);
        
        // Create data series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(finalVitalType);
        
        // Ensure vitalRecordsByType is initialized
        if (vitalRecordsByType == null) {
            System.out.println("WARNING: vitalRecordsByType is null, initializing empty map");
            vitalRecordsByType = new HashMap<>();
        }
        
        // Get records for this vital type
        List<VitalRecord> records = vitalRecordsByType.get(finalVitalType);
        System.out.println("Records for " + finalVitalType + ": " + (records != null ? records.size() : "null"));
        
        if (records == null || records.isEmpty()) {
            System.out.println("No records found for vital type: " + finalVitalType + ", using sample data");
            // Use sample data instead
            generateSampleData(series, finalVitalType);
        } else {
            // Use real data
            System.out.println("Found " + records.size() + " records for vital type: " + finalVitalType);
            
            // Get the earliest date for reference point (day 0)
            LocalDateTime referenceDate = records.get(0).getVitalReport().getReportWhenMade();
            System.out.println("Reference date: " + referenceDate);
            
            // Add data points
            for (int i = 0; i < records.size(); i++) {
                VitalRecord record = records.get(i);
                LocalDateTime recordDate = record.getVitalReport().getReportWhenMade();
                
                // Calculate days since reference date
                long daysSinceReference = ChronoUnit.DAYS.between(referenceDate, recordDate);
                
                // Add data point
                series.getData().add(new XYChart.Data<>(daysSinceReference, record.getValue()));
                System.out.println("Added data point: day " + daysSinceReference + ", value " + record.getValue());
            }
        }
        
        // Add the series to the chart
        System.out.println("Adding series to chart with " + series.getData().size() + " data points");
        vitalsChart.getData().add(series);
        System.out.println("Chart update completed");
        System.out.println("============================================");
    }
    
    /**
     * Generates sample data for a vital type when real data is not available
     * 
     * @param series The chart series to populate
     * @param vitalType The vital type
     */
    private void generateSampleData(XYChart.Series<Number, Number> series, String vitalType) {
        // For demo purposes, generate random data within the normal range for the selected vital type
        double[] range = getRangeForVitalType(vitalType);
        
        // Check if range is available
        if (range == null) {
            System.err.println("No range found for vital type: " + vitalType);
            // Use default range if missing
            range = new double[]{0, 100};
        }
        
        Random random = new Random();
        
        // Create 30 days of data points
        int numberOfDays = 30;
        
        // Create data points
        for (int i = 0; i < numberOfDays; i++) {
            // Generate a value that's mostly within range but occasionally outside
            double baseValue = range[0] + (range[1] - range[0]) * random.nextDouble();
            
            // Occasionally add abnormal values (20% chance)
            if (random.nextDouble() < 0.2) {
                // 50/50 chance of being above or below normal range
                if (random.nextBoolean()) {
                    baseValue = range[1] + (range[1] - range[0]) * random.nextDouble() * 0.3; // Above normal
                } else {
                    baseValue = Math.max(0, range[0] - (range[1] - range[0]) * random.nextDouble() * 0.3); // Below normal
                }
            }
            
            // Add some variability within a day
            series.getData().add(new XYChart.Data<>(i, Math.round(baseValue * 10) / 10.0));
        }
    }
    
    /**
     * Handles the update button click event.
     * Updates the chart with new parameters.
     * 
     * @param event The action event
     */
    @FXML
    private void handleUpdate(ActionEvent event) {
        System.out.println("============================================");
        System.out.println("Update button clicked");
        
        // Ensure a vital type is selected
        if (vitalTypeComboBox.getValue() == null) {
            System.out.println("WARNING: No vital type selected, attempting to set default");
            if (!vitalTypeComboBox.getItems().isEmpty()) {
                String defaultType = vitalTypeComboBox.getItems().get(0);
                System.out.println("Setting default vital type to: " + defaultType);
                vitalTypeComboBox.setValue(defaultType);
                System.out.println("Current value is now: " + vitalTypeComboBox.getValue());
            } else {
                System.out.println("ERROR: No items in dropdown to set as default");
            }
        } else {
            System.out.println("Current vital type: " + vitalTypeComboBox.getValue());
        }
        
        updateChart();
        System.out.println("============================================");
    }
    
    /**
     * Handles the "Show All Vitals" button click.
     * Displays all vital types on the chart at once.
     * 
     * @param event The action event
     */
    @FXML
    private void handleShowAll(ActionEvent event) {
        System.out.println("============================================");
        System.out.println("Show All Vitals button clicked");
        
        try {
            // Check if the chart is valid
            if (vitalsChart == null) {
                System.err.println("ERROR: Chart is null in handleShowAll");
                return;
            }
            
            // Clear previous data
            System.out.println("Clearing previous chart data");
            vitalsChart.getData().clear();
            
            // Configure chart for multiple series
            System.out.println("Configuring chart for multiple series");
            vitalsChart.setTitle("All Vital Signs Comparison");
            
            // Set y-axis to auto-ranging for multiple series
            yAxis.setAutoRanging(true);
            yAxis.setLabel("Value (Normalized)");
            System.out.println("Y-axis configured for normalized values");
            
            // Check if vital records map is valid
            if (vitalRecordsByType == null) {
                System.out.println("WARNING: vitalRecordsByType is null in handleShowAll, initializing empty map");
                vitalRecordsByType = new HashMap<>();
            }
            
            // Get a reference date (earliest date from all reports, if available)
            LocalDateTime referenceDate = null;
            if (patientVitalReports != null && !patientVitalReports.isEmpty()) {
                // Get the oldest report's date
                List<VitalReport> sortedReports = new ArrayList<>(patientVitalReports);
                sortedReports.sort(Comparator.comparing(VitalReport::getReportWhenMade));
                if (!sortedReports.isEmpty() && sortedReports.get(0) != null) {
                    referenceDate = sortedReports.get(0).getReportWhenMade();
                }
            }
            
            // If no reference date, use current date minus 30 days
            if (referenceDate == null) {
                referenceDate = LocalDateTime.now().minusDays(30);
            }
            
            // Final reference date for use in lambda
            final LocalDateTime finalReferenceDate = referenceDate;
            
            // Create a series for each vital type
            for (int i = 0; i < vitalTypes.length; i++) {
                String vitalType = vitalTypes[i];
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(vitalType);
                
                // Get the range for this vital type
                double[] range = vitalRanges[i];
                
                // Get records for this vital type
                List<VitalRecord> records = vitalRecordsByType.get(vitalType);
                
                if (records == null || records.isEmpty()) {
                    // Generate sample data for this vital type
                    Random random = new Random();
                    int numberOfDays = 30;
                    
                    for (int day = 0; day < numberOfDays; day++) {
                        // Generate a value that's mostly within range
                        double baseValue = range[0] + (range[1] - range[0]) * random.nextDouble();
                        
                        // Occasionally add abnormal values (10% chance)
                        if (random.nextDouble() < 0.1) {
                            if (random.nextBoolean()) {
                                baseValue = range[1] + (range[1] - range[0]) * random.nextDouble() * 0.2; // Above normal
                            } else {
                                baseValue = Math.max(0, range[0] - (range[1] - range[0]) * random.nextDouble() * 0.2); // Below normal
                            }
                        }
                        
                        // Normalize value for better visualization
                        double normalizedValue = (baseValue - range[0]) / (range[1] - range[0]) * 100;
                        
                        series.getData().add(new XYChart.Data<>(day, Math.round(normalizedValue * 10) / 10.0));
                    }
                } else {
                    // Use actual data from the patient's vital records
                    for (VitalRecord record : records) {
                        // Calculate days since reference date
                        long daysSinceReference = ChronoUnit.DAYS.between(
                            finalReferenceDate, record.getVitalReport().getReportWhenMade());
                        
                        // Normalize value to percentage of normal range
                        double normalizedValue = (record.getValue() - range[0]) / (range[1] - range[0]) * 100;
                        
                        // Add data point
                        series.getData().add(new XYChart.Data<>(daysSinceReference, Math.round(normalizedValue * 10) / 10.0));
                    }
                }
                
                // Add the series to the chart
                vitalsChart.getData().add(series);
            }
        } catch (Exception e) {
            System.err.println("********************************************");
            System.err.println("ERROR in handleShowAll: " + e.getMessage());
            e.printStackTrace();
            System.err.println("********************************************");
            showErrorAlert("Error", "Chart Error", "Error displaying all vitals: " + e.getMessage());
        }
        System.out.println("============================================");
    }
    
    /**
     * Handles the back button click event.
     * Navigates back to the view vitals screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.VIEW_VITALS, ScreenPaths.TITLE_VIEW_VITALS);
    }
} 