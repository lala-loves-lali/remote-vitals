package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.application.Platform;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

/**
 * Controller for the vitals visualization screen.
 * Displays graphs of patient vital data over time.
 */
public class VitalsGraphController extends BaseController {

    @FXML
    private LineChart<Number, Number> vitalsChart;
    
    @FXML
    private NumberAxis xAxis;
    
    @FXML
    private NumberAxis yAxis;
    
    @FXML
    private ComboBox<String> vitalTypeComboBox;
    
    @FXML
    private DatePicker startDatePicker;
    
    @FXML
    private DatePicker endDatePicker;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button backButton;
    
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
    
    private String currentPatientId = "P12345"; // For demo purposes
    private String currentPatientName = "John Doe"; // For demo purposes
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Set patient info
        patientNameLabel.setText(currentPatientName);
        patientIdLabel.setText("ID: " + currentPatientId);
        
        // Print vital types for debugging
        System.out.println("Vital types in array format:");
        for (int i = 0; i < vitalTypes.length; i++) {
            System.out.println(i + ": " + vitalTypes[i] + ", Range: " + Arrays.toString(vitalRanges[i]));
        }
        
        // Create observable list directly from array
        final ObservableList<String> vitalTypesList = FXCollections.observableArrayList(vitalTypes);
        System.out.println("Observable list created with " + vitalTypesList.size() + " items");
        
        try {
            // Set items directly on the ComboBox
            vitalTypeComboBox.getItems().clear();
            vitalTypeComboBox.getItems().addAll(vitalTypesList);
            System.out.println("Direct items add completed. ComboBox has " + vitalTypeComboBox.getItems().size() + " items");
            
            // Force a default selection
            if (!vitalTypesList.isEmpty()) {
                vitalTypeComboBox.setValue(vitalTypesList.get(0));
                System.out.println("Default value set to: " + vitalTypeComboBox.getValue());
            }
        } catch (Exception e) {
            System.err.println("Error setting ComboBox items: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Also use Platform.runLater as a backup approach
        Platform.runLater(() -> {
            try {
                // Double-check if items were already set
                if (vitalTypeComboBox.getItems().isEmpty()) {
                    vitalTypeComboBox.setItems(vitalTypesList);
                }
                
                // Force selection again if needed
                if (vitalTypeComboBox.getValue() == null) {
                    vitalTypeComboBox.getSelectionModel().selectFirst();
                }
                
                System.out.println("Platform.runLater completed. ComboBox has " + 
                    vitalTypeComboBox.getItems().size() + " items, selected: " + 
                    vitalTypeComboBox.getValue());
            } catch (Exception e) {
                System.err.println("Error in Platform.runLater: " + e.getMessage());
                e.printStackTrace();
            }
        });
        
        // Add listener for debugging dropdown issues
        vitalTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Vital type changed from: " + oldValue + " to: " + newValue);
            if (newValue != null) {
                updateYAxisForVitalType(newValue);
                updateChart();
            }
        });
        
        // Ensure ComboBox is properly configured
        vitalTypeComboBox.setVisible(true);
        vitalTypeComboBox.setDisable(false);
        
        // Initialize date pickers
        LocalDate now = LocalDate.now();
        endDatePicker.setValue(now);
        startDatePicker.setValue(now.minusDays(30));
        
        // Configure chart
        configureChart();
        
        // Initial data load
        updateChart();
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
            if (!vitalTypeComboBox.getItems().isEmpty()) {
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
     * Updates the chart with new data based on selected parameters.
     */
    private void updateChart() {
        String vitalType = vitalTypeComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        
        System.out.println("updateChart called with vitalType: " + vitalType);
        
        if (vitalType == null || startDate == null || endDate == null) {
            System.out.println("Cannot update chart - null values: vitalType=" + vitalType + 
                               ", startDate=" + startDate + ", endDate=" + endDate);
            
            // Try to recover if possible
            if (vitalType == null && !vitalTypeComboBox.getItems().isEmpty()) {
                vitalType = vitalTypeComboBox.getItems().get(0);
                vitalTypeComboBox.setValue(vitalType);
                System.out.println("Recovered vitalType to: " + vitalType);
            } else {
                return; // Cannot proceed with null values
            }
        }
        
        if (endDate.isBefore(startDate)) {
            showErrorAlert("Date Range Error", "Invalid Date Range", 
                    "End date must be after start date.");
            return;
        }
        
        // Clear previous data
        vitalsChart.getData().clear();
        
        // Update y-axis for the selected vital type
        updateYAxisForVitalType(vitalType);
        
        // Create data series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(vitalType);
        
        // For demo purposes, generate random data within the normal range for the selected vital type
        double[] range = getRangeForVitalType(vitalType);
        
        // Check if range is available
        if (range == null) {
            System.err.println("No range found for vital type: " + vitalType);
            // Use default range if missing
            range = new double[]{0, 100};
        }
        
        Random random = new Random();
        
        // Calculate days between dates
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        
        // Create data points
        for (int i = 0; i < daysBetween; i++) {
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
        
        // Add the series to the chart
        vitalsChart.getData().add(series);
    }
    
    /**
     * Handles the update button click event.
     * Updates the chart with new parameters.
     * 
     * @param event The action event
     */
    @FXML
    private void handleUpdate(ActionEvent event) {
        updateChart();
    }
    
    /**
     * Sets the vital data to be displayed as a line chart.
     * This method can be called from other controllers to set the data.
     * 
     * @param vitalData Array of vital measurement values
     * @param vitalType Type of vital measurement
     */
    public void setVitalData(double[] vitalData, String vitalType) {
        if (vitalData == null || vitalData.length == 0 || vitalType == null) {
            return;
        }
        
        // Clear previous data
        vitalsChart.getData().clear();
        
        // Select the vital type in the combo box if it exists
        if (getVitalTypeIndex(vitalType) >= 0) {
            vitalTypeComboBox.setValue(vitalType);
            updateYAxisForVitalType(vitalType);
        }
        
        // Create data series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(vitalType);
        
        // Add data points
        for (int i = 0; i < vitalData.length; i++) {
            series.getData().add(new XYChart.Data<>(i, vitalData[i]));
        }
        
        // Add the series to the chart
        vitalsChart.getData().add(series);
    }
    
    /**
     * Sets multiple vital data arrays to be displayed as multiple series on a line chart.
     * This method can be called from other controllers to set multiple data series.
     * 
     * @param vitalDataArrays 2D array where each row is a separate vital data array
     * @param vitalTypes Array of vital types corresponding to each data array
     * @param patientName Name of the patient whose data is being displayed
     * @param patientId ID of the patient whose data is being displayed
     */
    public void setMultipleVitalData(double[][] vitalDataArrays, String[] vitalTypes, String patientName, String patientId) {
        if (vitalDataArrays == null || vitalTypes == null || 
            vitalDataArrays.length == 0 || vitalTypes.length == 0 ||
            vitalDataArrays.length != vitalTypes.length) {
            showErrorAlert("Data Error", "Invalid Vital Data", 
                    "The vital data arrays and types must have the same length.");
            return;
        }
        
        // Update patient info
        if (patientName != null && !patientName.isEmpty()) {
            patientNameLabel.setText(patientName);
            currentPatientName = patientName;
        }
        
        if (patientId != null && !patientId.isEmpty()) {
            patientIdLabel.setText("ID: " + patientId);
            currentPatientId = patientId;
        }
        
        // Clear previous data
        vitalsChart.getData().clear();
        
        // Configure chart for multiple series
        vitalsChart.setTitle("Multiple Vital Signs Comparison");
        
        // Set y-axis to auto-ranging for multiple series
        yAxis.setAutoRanging(true);
        yAxis.setLabel("Value");
        
        // Add each data series to the chart
        for (int i = 0; i < vitalDataArrays.length; i++) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(vitalTypes[i]);
            
            for (int j = 0; j < vitalDataArrays[i].length; j++) {
                series.getData().add(new XYChart.Data<>(j, vitalDataArrays[i][j]));
            }
            
            vitalsChart.getData().add(series);
        }
    }
    
    /**
     * Updates the patient information on the chart.
     * 
     * @param patientName Name of the patient
     * @param patientId ID of the patient
     */
    public void setPatientInfo(String patientName, String patientId) {
        if (patientName != null && !patientName.isEmpty()) {
            patientNameLabel.setText(patientName);
            currentPatientName = patientName;
        }
        
        if (patientId != null && !patientId.isEmpty()) {
            patientIdLabel.setText("ID: " + patientId);
            currentPatientId = patientId;
        }
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