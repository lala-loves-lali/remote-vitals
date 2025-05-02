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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
    
    // Map to store different vital types and their normal ranges
    private Map<String, double[]> vitalRanges = new HashMap<>();
    
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
        
        // Initialize vital types and their normal ranges [min, max]
        vitalRanges.put("Heart Rate (BPM)", new double[]{60, 100});
        vitalRanges.put("Blood Pressure - Systolic (mmHg)", new double[]{90, 120});
        vitalRanges.put("Blood Pressure - Diastolic (mmHg)", new double[]{60, 80});
        vitalRanges.put("Temperature (°C)", new double[]{36.1, 37.2});
        vitalRanges.put("Respiratory Rate (breaths/min)", new double[]{12, 20});
        vitalRanges.put("Oxygen Saturation (%)", new double[]{95, 100});
        
        // Initialize vital type dropdown
        vitalTypeComboBox.setItems(FXCollections.observableArrayList(vitalRanges.keySet()));
        vitalTypeComboBox.setValue("Heart Rate (BPM)");
        
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
        updateYAxisForVitalType(vitalTypeComboBox.getValue());
    }
    
    /**
     * Updates the y-axis scale and label based on the selected vital type.
     * 
     * @param vitalType The selected vital type
     */
    private void updateYAxisForVitalType(String vitalType) {
        yAxis.setLabel(vitalType);
        
        // Set y-axis range based on vital type
        double[] range = vitalRanges.get(vitalType);
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
        
        if (vitalType == null || startDate == null || endDate == null) {
            return;
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
        double[] range = vitalRanges.get(vitalType);
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
        if (vitalRanges.containsKey(vitalType)) {
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