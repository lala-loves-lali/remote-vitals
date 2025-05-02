package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Controller for the upload vitals data screen.
 * Handles uploading and managing patient vitals data.
 */
public class UploadVitalsController extends BaseController {

    @FXML
    private TextField filePathField;
    
    @FXML
    private Button browseButton;
    
    @FXML
    private Button uploadButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private TextArea statusTextArea;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize UI components
        if (statusTextArea != null) {
            statusTextArea.setEditable(false);
            statusTextArea.setText("Ready to upload vitals data.");
        }
    }
    
    /**
     * Handles the browse button click event.
     * Opens a file chooser dialog to select a vitals data file.
     * 
     * @param event The action event
     */
    @FXML
    private void handleBrowse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Vitals Data File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        Stage stage = getStageFromEvent(event);
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
            appendStatus("File selected: " + selectedFile.getName());
        }
    }
    
    /**
     * Handles the upload button click event.
     * Uploads the selected vitals data file.
     * 
     * @param event The action event
     */
    @FXML
    private void handleUpload(ActionEvent event) {
        String filePath = filePathField.getText().trim();
        
        if (filePath.isEmpty()) {
            showErrorAlert("Upload Error", "No File Selected", 
                    "Please select a file to upload.");
            return;
        }
        
        // In a real application, this would connect to a service to upload the file
        // For now, we'll just simulate a successful upload
        
        appendStatus("Uploading file: " + filePath);
        appendStatus("Processing data...");
        appendStatus("Upload completed successfully.");
        
        showInfoAlert("Upload Success", "Data Uploaded", 
                "Vitals data has been uploaded successfully.");
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
    
    /**
     * Appends a message to the status text area.
     * 
     * @param message The message to append
     */
    private void appendStatus(String message) {
        if (statusTextArea != null) {
            statusTextArea.appendText("\n" + message);
        }
    }
} 