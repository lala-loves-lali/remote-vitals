package com.remote_vitals.frontend.controllers;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller for the admin dashboard screen.
 * Handles admin-specific functionality and navigation.
 */
public class AdminDashboardController extends BaseController {

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Button profileButton;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize UI components or load data if needed
    }
    
    /**
     * Handles the dashboard button click event.
     * Refreshes the dashboard view.
     * 
     * @param event The action event
     */
    @FXML
    private void handleDashboard(ActionEvent event) {
        // Refresh dashboard stats
        showInfoAlert("Dashboard Refresh", "Dashboard", 
                "Dashboard has been refreshed.");
    }
    
    /**
     * Handles the manage users button click event.
     * Opens the user management screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleManageUsers(ActionEvent event) {
        // To be implemented with the user management screen
        showInfoAlert("Feature Coming Soon", "Manage Users", 
                "This feature will be available in a future update.");
    }
    
    /**
     * Handles the view system logs button click event.
     * Shows the system logs.
     * 
     * @param event The action event
     */
    @FXML
    private void handleViewSystemLogs(ActionEvent event) {
        // To be implemented with the system logs screen
        showInfoAlert("Feature Coming Soon", "System Logs", 
                "This feature will be available in a future update.");
    }
    
    /**
     * Handles the reports button click event.
     * Opens the reports screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleReports(ActionEvent event) {
        // To be implemented with the reports screen
        showInfoAlert("Feature Coming Soon", "Reports", 
                "This feature will be available in a future update.");
    }
    
    /**
     * Handles the system settings button click event.
     * Opens the system settings screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleSystemSettings(ActionEvent event) {
        // To be implemented with the system settings screen
        showInfoAlert("Feature Coming Soon", "System Settings", 
                "This feature will be available in a future update.");
    }
    
    /**
     * Handles the profile button click event.
     * Navigates to the admin profile screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleProfile(ActionEvent event) {
        navigateTo(event, ScreenPaths.ADMIN_PROFILE, ScreenPaths.TITLE_ADMIN_PROFILE);
    }
    
    /**
     * Handles the logout button click event.
     * Navigates back to the login screen.
     * 
     * @param event The action event
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        navigateTo(event, ScreenPaths.LOGIN_PAGE, ScreenPaths.TITLE_LOGIN);
    }
    
    /**
     * Handles the dashboard selector button click event.
     * Navigates to the dashboard selector for testing.
     * 
     * @param event The action event
     */
    @FXML
    private void handleDashboardSelector(ActionEvent event) {
        navigateTo(event, ScreenPaths.DASHBOARD_SELECTOR, ScreenPaths.TITLE_DASHBOARD_SELECTOR);
    }
} 