package com.remote_vitals.frontend.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.remote_vitals.backend.services.LoginService;
import com.remote_vitals.backend.user.entities.Admin;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.entities.User;
import com.remote_vitals.frontend.utils.ScreenPaths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for the login page.
 * Handles user authentication and navigation to the appropriate dashboard.
 */
public class LoginController extends BaseController {
    @FXML
    private TextField email_input;
    
    @FXML
    private PasswordField password_input;
    
    @FXML
    private Button login_button;
    
    @FXML
    private Hyperlink signupLink;
    
    @FXML
    private Hyperlink forgotPasswordLink;
    
    @FXML
    private ChoiceBox<String> userTypeChoice;
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        
        // Initialize user type dropdown
        userTypeChoice.getItems().clear(); // Clear any existing items
        userTypeChoice.getItems().addAll("Patient", "Doctor", "Admin");
        userTypeChoice.setValue("Patient");
        
        // Add listener to pre-fill email with a demo value based on the selected user type
        userTypeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                switch (newVal) {
                    case "Patient":
                        email_input.setPromptText("patient@example.com");
                        break;
                    case "Doctor":
                        email_input.setPromptText("doctor@example.com");
                        break;
                    case "Admin":
                        email_input.setPromptText("admin@example.com");
                        break;
                }
            }
        });
        
        // Force the prompt text to update initially
        email_input.setPromptText("patient@example.com");
        
        // Add some debug info to help track issues
        userTypeChoice.setOnShowing(event -> {
            System.out.println("ChoiceBox dropdown is showing");
        });
        
        userTypeChoice.setOnShown(event -> {
            System.out.println("ChoiceBox dropdown is shown");
        });
        
        userTypeChoice.setOnHidden(event -> {
            System.out.println("ChoiceBox dropdown is hidden");
        });
    }
    
    /**
     * Handles the login button click event.
     * Authenticates the user and navigates to the appropriate dashboard.
     * 
     * @param event The action event
     */
    @FXML
    private void handleLogin(ActionEvent event) {
       String email = email_input.getText().trim();
       String password = password_input.getText().trim();
       String userType = userTypeChoice.getValue();

       // Input validation
       if (email.isEmpty() || password.isEmpty()) {
           showErrorAlert("Login Error", "Missing Information",
                   "Please enter both email and password.");
           return;
       }

       // Simple password validation
       if (password.length() < 4) {
           showErrorAlert("Login Error", "Invalid Password",
                   "Password must be at least 4 characters long.");
           return;
       }

       User user;
       if(getContext()==null){
           return;
       }

       switch(userType){
        case "Patient":
            try{
                getContext().getBean(LoginService.class).login(email, password, Patient.class);
                navigateTo(event, ScreenPaths.PATIENT_DASHBOARD, ScreenPaths.TITLE_PATIENT_DASHBOARD);
            }
            catch(RuntimeException e){
                showErrorAlert("Login Error", "Authentication Failed",
                        "Invalid email or password. Please try again.");
                return;
            }

            break;
        case "Doctor":
            try{
                getContext().getBean(LoginService.class).login(email, password, Doctor.class);
                navigateTo(event, ScreenPaths.DOCTOR_DASHBOARD, ScreenPaths.TITLE_DOCTOR_DASHBOARD);
            }
            catch(RuntimeException e){
                
                return;
            }
        case "Admin":
            try{
                getContext().getBean(LoginService.class).login(email, password, Admin.class);
                navigateTo(event, ScreenPaths.ADMIN_DASHBOARD, ScreenPaths.TITLE_ADMIN_DASHBOARD);
            }
            catch(RuntimeException e){
                showErrorAlert("Login Error", "Authentication Failed",
                        "Invalid email or password. Please try again.");
                return;
            }
            break;
       }
       

       
       
       
    }
    
    /**
     * Handles the sign up hyperlink click event.
     * Navigates to the sign up page.
     * 
     * @param event The action event
     */
    @FXML
    private void handleSignup(ActionEvent event) {
        navigateTo(event, ScreenPaths.SIGNUP_PAGE, ScreenPaths.TITLE_SIGNUP);
    }
    
    /**
     * Handles the forgot password hyperlink click event.
     * Shows a dialog for password reset.
     * 
     * @param event The action event
     */
    @FXML
    private void handleForgotPassword(ActionEvent event) {
        String email = email_input.getText().trim();
        
        // if (email.isEmpty()) {
        //     showInfoAlert("Password Reset", "Email Required", 
        //             "Please enter your email address in the email field first.");
        // } else {
        //     User user = BaseController.getDb().getUserFromEmail(email)   ;
        //     if(user.getPassword().equals(password)){
        //         showInfoAlert("Password Reset", "Reset Link Sent", 
        //             "If an account exists with email " + email + ", a password reset link has been sent. Please check your email.");
        //     }
        //     else{
        //         showErrorAlert("Password Reset", "Invalid Password", 
        //             "Invalid password. Please try again.");
        //     }

        //     // In a real application, this would send a password reset email
        //     showInfoAlert("Password Reset", "Reset Link Sent", 
        //             "If an account exists with email " + email + ", a password reset link has been sent. Please check your email.");
        // }


    }
} 