package com.remote_vitals.frontend.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


import com.remote_vitals.backend.services.UserService;
import com.remote_vitals.backend.user.entities.Admin;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.entities.User;

import com.remote_vitals.frontend.utils.NavigationUtil;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Base controller class that provides common functionality for all controllers.
 * This class handles common operations like navigation and manages stage access.
 */
public abstract class BaseController implements Initializable {

    protected static ConfigurableApplicationContext context;

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

    public static void setContext(ConfigurableApplicationContext context) {
        BaseController.context = context;
    }

    
    /**
     * Default implementation of the initialize method.
     * Child controllers should override this method and call super.initialize() first.
     * 
     * @param location The location used to resolve relative paths for the root object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Base initialization, can be overridden by subclasses
    }
    
    /**
     * Enables responsive behavior for the current scene.
     * Should be called after the scene is fully loaded, typically in a controller method
     * that's invoked after navigation, such as a custom "onSceneLoaded" method.
     */
    protected void setupResponsiveScene(Scene scene) {
        if (scene != null && scene.getRoot() != null) {
            NavigationUtil.makeResponsive(scene.getRoot(), scene);
        }
    }
    
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
    

    public static Doctor getDoctorUser(){
     
        if (getCurrentUser()!= null && getCurrentUser().isPresent() && getCurrentUser().get() instanceof Doctor){
            return (Doctor) getCurrentUser().get();
        }
        System.out.println("No doctor user found,In base controller, getDoctorUser()");
        return null;
    }

    public static Admin getAdminUser(){
        if (getCurrentUser()!= null && getCurrentUser().isPresent() && getCurrentUser().get() instanceof Admin){
            return (Admin) getCurrentUser().get();
        }
        System.out.println("No admin user found,In base controller, getAdminUser()");
        return null;
    }
    
    public static Patient getPatientUser(){
        if (getCurrentUser()!= null && getCurrentUser().isPresent() && getCurrentUser().get() instanceof Patient){
            return (Patient) getCurrentUser().get();
        }
        System.out.println("No patient user found,In base controller, getPatientUser()");
        return null;
    }

//
//    public static boolean isDoctorUser(){
//        if (getCurrentUser()== null){
//            return false;
//        }
//        if (userType == UserType.DOCTOR){
//            return true;
//        }
//
//        return false;
//    }
//
//
//    public static Admin getAdminUser(){
//        if (getCurrentUser()== null){
//            return null;
//        }
//        if (userType == UserType.ADMIN){
//            return (Admin) getCurrentUser();
//        }
//
//        return null;
//    }
//
//    public static Patient getPatientUser(){
//        if (getCurrentUser()== null){
//            return null;
//        }
//        if (userType == UserType.PATIENT){
//            return (Patient) getCurrentUser();
//        }
//
//        return null;
//    }
//
//
//
//    public static UserType getUserType() {
//        return userType;
//    }
//
//    public static void setUserType(UserType userType) {
//        BaseController.userType = userType;
//    }
//
   public static Optional<User> getCurrentUser() {
       return getContext().getBean(UserService.class).getCurrentUser();
   }
//
//    public static void setCurrentUs
//    public static void setDb(DataBaseHandler db) {
//        BaseController.db = db;
//    }er(User currentUser) {
//        BaseController.currentUser = currentUser;
//    }
//
//
//    public static DataBaseHandler getDb() {
//        return db;
//    }

}