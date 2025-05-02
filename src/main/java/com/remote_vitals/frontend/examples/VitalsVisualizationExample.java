package com.remote_vitals.frontend.examples;

import com.remote_vitals.frontend.controllers.VitalsGraphController;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

/**
 * Example class that demonstrates how to use the VitalsGraphController
 * to visualize different types of vital data.
 */
public class VitalsVisualizationExample extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the vitals graph FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ScreenPaths.VITALS_GRAPH));
        Scene scene = new Scene(loader.load());
        
        // Get the controller
        VitalsGraphController controller = loader.getController();
        
        // Set the stage and show it
        stage.setTitle(ScreenPaths.TITLE_VITALS_GRAPH);
        stage.setScene(scene);
        stage.show();
        
        // Example 1: Set a single vital data array
        // Simulate heart rate data for 30 days
        demonstrateSingleVitalVisualization(controller);
        
        // Example 2: Set multiple vital data arrays
        // After 5 seconds, switch to multiple vitals view
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                javafx.application.Platform.runLater(() -> {
                    demonstrateMultipleVitalsVisualization(controller);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    /**
     * Demonstrates single vital data visualization.
     * 
     * @param controller The VitalsGraphController
     */
    private void demonstrateSingleVitalVisualization(VitalsGraphController controller) {
        // Generate heart rate data (30 days)
        double[] heartRateData = generateRandomHeartRateData(30);
        
        // Set the data in the controller
        controller.setVitalData(heartRateData, "Heart Rate (BPM)");
        
        // Set patient info
        controller.setPatientInfo("John Doe", "P12345");
    }
    
    /**
     * Demonstrates multiple vital data visualization.
     * 
     * @param controller The VitalsGraphController
     */
    private void demonstrateMultipleVitalsVisualization(VitalsGraphController controller) {
        // Generate data for multiple vitals (30 days each)
        double[] heartRateData = generateRandomHeartRateData(30);
        double[] temperatureData = generateRandomTemperatureData(30);
        double[] oxygenSaturationData = generateRandomOxygenSaturationData(30);
        
        // Create a 2D array of vital data
        double[][] vitalDataArrays = new double[][] {
            heartRateData,
            temperatureData,
            oxygenSaturationData
        };
        
        // Create an array of vital types
        String[] vitalTypes = new String[] {
            "Heart Rate (BPM)",
            "Temperature (°C)",
            "Oxygen Saturation (%)"
        };
        
        // Set the data in the controller
        controller.setMultipleVitalData(vitalDataArrays, vitalTypes, "Jane Smith", "P67890");
    }
    
    /**
     * Generates random heart rate data between 55 and 120 BPM.
     * 
     * @param days The number of days to generate data for
     * @return An array of heart rate values
     */
    private double[] generateRandomHeartRateData(int days) {
        Random random = new Random();
        double[] data = new double[days];
        
        double baseHeartRate = 70.0; // Average heart rate
        
        for (int i = 0; i < days; i++) {
            // Generate heart rate with some variability around the base value
            data[i] = baseHeartRate + (random.nextDouble() * 20.0 - 10.0);
            
            // Occasionally add abnormal values
            if (random.nextDouble() < 0.1) {
                if (random.nextBoolean()) {
                    data[i] = 100.0 + random.nextDouble() * 20.0; // High heart rate
                } else {
                    data[i] = 55.0 - random.nextDouble() * 5.0; // Low heart rate
                }
            }
            
            // Round to one decimal place
            data[i] = Math.round(data[i] * 10.0) / 10.0;
        }
        
        return data;
    }
    
    /**
     * Generates random temperature data between 35.8 and 38.0 degrees Celsius.
     * 
     * @param days The number of days to generate data for
     * @return An array of temperature values
     */
    private double[] generateRandomTemperatureData(int days) {
        Random random = new Random();
        double[] data = new double[days];
        
        double baseTemperature = 36.8; // Normal body temperature
        
        for (int i = 0; i < days; i++) {
            // Generate temperature with small variability around the base value
            data[i] = baseTemperature + (random.nextDouble() * 0.4 - 0.2);
            
            // Occasionally add fever or low temperature
            if (random.nextDouble() < 0.1) {
                if (random.nextBoolean()) {
                    data[i] = 37.5 + random.nextDouble() * 0.5; // Fever
                } else {
                    data[i] = 36.0 - random.nextDouble() * 0.2; // Low temperature
                }
            }
            
            // Round to one decimal place
            data[i] = Math.round(data[i] * 10.0) / 10.0;
        }
        
        return data;
    }
    
    /**
     * Generates random oxygen saturation data between 90 and 100 percent.
     * 
     * @param days The number of days to generate data for
     * @return An array of oxygen saturation values
     */
    private double[] generateRandomOxygenSaturationData(int days) {
        Random random = new Random();
        double[] data = new double[days];
        
        double baseOxygenSaturation = 98.0; // Normal oxygen saturation
        
        for (int i = 0; i < days; i++) {
            // Generate oxygen saturation with small variability around the base value
            data[i] = baseOxygenSaturation + (random.nextDouble() * 2.0 - 1.0);
            
            // Occasionally add low oxygen levels
            if (random.nextDouble() < 0.1) {
                data[i] = 92.0 - random.nextDouble() * 2.0; // Low oxygen saturation
            }
            
            // Round to one decimal place
            data[i] = Math.round(data[i] * 10.0) / 10.0;
        }
        
        return data;
    }
    
    /**
     * Main method to launch the example application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
} 