package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller for the main view screen.
 * This is the entry point of the application UI.
 */
public class MainViewController extends BaseController {

    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialization code goes here
    }
    
    /**
     * Handles the login button click event.
     * Navigates to the login screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        // Navigate to the login page
        navigateTo(event, ScreenPaths.LOGIN_PAGE, ScreenPaths.TITLE_LOGIN);
    }
    
    /**
     * Exits the application when the exit menu item is clicked.
     * 
     * @param event The action event
     */
    @FXML
    private void exitApplication(ActionEvent event) {
        Platform.exit();
    }
} 