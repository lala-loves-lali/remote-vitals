package com.remote_vitals.frontend.controllers;

import com.remote_vitals.backend.db_handler.StaticDataClass;
import com.remote_vitals.backend.services.VitalReportService;
import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UploadVitalsController extends BaseController {

    @FXML private TextField filePathField; // Binds to the TextField in FXML
    @FXML private Button browseButton;
    @FXML private Button uploadButton;
    @FXML private Button backButton;
    @FXML private TextArea statusTextArea;
    private static final String SAVE_DIRECTORY = "C:\\Users\\Ibsham Tariq\\Desktop\\saved";

    @FXML
    private void initialize() {
        // Initialize UI components
        if (statusTextArea != null) {
            statusTextArea.setEditable(false);
            statusTextArea.setText("Ready to upload vitals data.");
        }
    }

    @FXML
    private void handleBrowse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Vitals Data File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = getStageFromEvent(event);
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
            appendStatus("File selected: " + selectedFile.getName());
        }
    }

    @FXML
    private void handleUpload(ActionEvent event) {
        String filePath = filePathField.getText().trim();
        if (filePath.isEmpty()) {
            showErrorAlert("File Error", "No File Selected", "Please select a CSV file to upload.");
            return;
        }

        File selectedFile = new File(filePath);
        if (!selectedFile.exists() || !selectedFile.isFile()) {
            showErrorAlert("File Error", "Invalid File", "The selected file could not be found.");
            return;
        }

        try {
            Files.createDirectories(Paths.get(SAVE_DIRECTORY));
            String currentUserId = (StaticDataClass.currentUserId != null)
                    ? StaticDataClass.currentUserId.toString()
                    : "unknown_user";
            String destinationFilePath = SAVE_DIRECTORY + File.separator + currentUserId + ".csv";

            System.out.println("Copying file to: " + destinationFilePath);
            copyCsvFile(selectedFile, new File(destinationFilePath));
            appendStatus("File successfully copied to: " + destinationFilePath);

            // Delegate parsing and validation to the VitalReportService
            String validationResponse = StaticDataClass.context1
                    .getBean(VitalReportService.class)
                    .readCsvForCurrentUser();

            if ("Csv Parsed Successfully".equals(validationResponse)) {
                showInfoAlert("Upload Success", "File Uploaded",
                        "The file has been successfully uploaded and processed.");
                appendStatus("Upload successful! Data has been processed.");
            } else {
                showErrorAlert("Upload Failed", "Invalid File Format",
                        "The uploaded file format is invalid. Please check the file and try again.");
                appendStatus("Error: Invalid file format detected during processing.");
            }
        } catch (IOException ex) {
            showErrorAlert("File Error", "Error Copying File",
                    "An error occurred while saving the file. Please try again.");
            appendStatus("Error: Unable to copy the file. Details: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            showErrorAlert("Upload Error", "Processing Failed",
                    "An error occurred while processing the file. Please try again.");
            appendStatus("Error: File processing failed. Details: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.VIEW_VITALS, ScreenPaths.TITLE_VIEW_VITALS);
    }

    /**
     * Copies the content of one CSV file to another.
     * Removes BOM (byte-order marker) if present in the source file.
     *
     * @param sourceFile      The source CSV file
     * @param destinationFile The destination CSV file
     * @throws IOException If an error occurs during file operations
     */
    private void copyCsvFile(File sourceFile, File destinationFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile))) {

            String line = reader.readLine();
            // Check and remove BOM if present
            if (line != null && line.startsWith("\uFEFF")) {
                line = line.substring(1); // Remove BOM
            }

            if (line != null) {
                writer.write(line);
                writer.newLine();
            }

            // Copy the remaining lines
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
        System.out.println("File copied successfully with BOM handled (if present).");
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