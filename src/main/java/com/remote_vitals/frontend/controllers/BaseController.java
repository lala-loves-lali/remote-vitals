package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Base controller class that provides common functionality for all controllers.
 * This class handles common operations like navigation and manages stage access.
 */
public abstract class BaseController {
    
    /**
     * Gets the current stage from an event's source.
     * 
     * @param event The event triggered by a UI component
     * @return The current stage
     */
    protected Stage getStageFromEvent(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
    
    /**
     * Navigates to a new screen by loading the specified FXML file.
     * 
     * @param event The event triggering the navigation
     * @param fxmlPath The path to the FXML file to load
     * @param title The title for the new window
     * @return The controller instance for the loaded FXML
     */
    protected <T> T navigateTo(ActionEvent event, String fxmlPath, String title) {
        Stage currentStage = getStageFromEvent(event);
        return NavigationUtil.navigateToScreen(currentStage, fxmlPath, title);
    }
    
    /**
     * Navigates to a new screen using a specific stage.
     * 
     * @param stage The stage to change the scene on
     * @param fxmlPath The path to the FXML file to load
     * @param title The title for the new window
     * @return The controller instance for the loaded FXML
     */
    protected <T> T navigateTo(Stage stage, String fxmlPath, String title) {
        return NavigationUtil.navigateToScreen(stage, fxmlPath, title);
    }
    
    /**
     * Opens a new window with the specified FXML content.
     * 
     * @param fxmlPath The path to the FXML file to load
     * @param title The title for the new window
     * @return The controller instance for the loaded FXML
     */
    protected <T> T openInNewWindow(String fxmlPath, String title) {
        return NavigationUtil.openInNewWindow(fxmlPath, title);
    }
    
    /**
     * Shows an error alert with the specified information.
     * 
     * @param title The title of the alert
     * @param header The header text for the alert
     * @param content The content text for the alert
     */
    protected void showErrorAlert(String title, String header, String content) {
        NavigationUtil.showErrorAlert(title, header, content);
    }
    
    /**
     * Shows an information alert with the specified information.
     * 
     * @param title The title of the alert
     * @param header The header text for the alert
     * @param content The content text for the alert
     */
    protected void showInfoAlert(String title, String header, String content) {
        NavigationUtil.showInfoAlert(title, header, content);
    }
} 