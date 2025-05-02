package com.remote_vitals.frontend.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Utility class for handling navigation between different screens in the application.
 * This class provides methods to switch scenes, show dialogs, and handle navigation errors.
 */
public class NavigationUtil {

    /**
     * Navigates to a new screen by loading the specified FXML file.
     * Replaces the current scene with the new one.
     *
     * @param currentStage The current stage to change the scene on
     * @param fxmlPath The path to the FXML file to load
     * @param title The title for the new window
     * @return The controller instance for the loaded FXML
     */
    public static <T> T navigateToScreen(Stage currentStage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            currentStage.setTitle(title);
            currentStage.setScene(scene);
            currentStage.show();
            
            return loader.getController();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Could not navigate to " + fxmlPath, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Creates a new window (stage) with the specified FXML content.
     *
     * @param fxmlPath The path to the FXML file to load
     * @param title The title for the new window
     * @return The controller instance for the loaded FXML
     */
    public static <T> T openInNewWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationUtil.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
            
            return loader.getController();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Could not open new window for " + fxmlPath, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Shows an error alert with the specified information.
     *
     * @param title The title of the alert
     * @param header The header text for the alert
     * @param content The content text for the alert
     */
    public static void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Shows an information alert with the specified information.
     *
     * @param title The title of the alert
     * @param header The header text for the alert
     * @param content The content text for the alert
     */
    public static void showInfoAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 