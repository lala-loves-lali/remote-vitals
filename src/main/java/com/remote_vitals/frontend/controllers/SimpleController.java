package com.remote_vitals.frontend.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A simple controller for testing purposes
 */
public class SimpleController {
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private Button testButton;
    
    @FXML
    private TextArea infoTextArea;
    
    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        messageLabel.setText("Application Started at: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // Display system information
        displaySystemInfo();
    }
    
    /**
     * Handle button click event
     */
    @FXML
    public void handleButtonClick(ActionEvent event) {
        messageLabel.setText("Button clicked at: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    /**
     * Display system information
     */
    private void displaySystemInfo() {
        if (infoTextArea != null) {
            StringBuilder info = new StringBuilder();
            info.append("System Information:\n");
            info.append("------------------\n");
            info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
            info.append("JavaFX Version: ").append(System.getProperty("javafx.version")).append("\n");
            info.append("OS: ").append(System.getProperty("os.name")).append(" ")
                .append(System.getProperty("os.version")).append("\n");
            info.append("User: ").append(System.getProperty("user.name")).append("\n");
            info.append("Working Directory: ").append(System.getProperty("user.dir")).append("\n\n");
            
            info.append("Module Information:\n");
            info.append("------------------\n");
            info.append("Is Modular: ").append(this.getClass().getModule() != null ? "Yes" : "No").append("\n");
            if (this.getClass().getModule() != null) {
                info.append("Module Name: ").append(this.getClass().getModule().getName()).append("\n");
                info.append("Is Named: ").append(this.getClass().getModule().isNamed()).append("\n");
            }
            
            infoTextArea.setText(info.toString());
        }
    }
} 