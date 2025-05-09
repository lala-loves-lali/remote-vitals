package com.remote_vitals.frontend.controllers;


import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.frontend.utils.ScreenPaths;
import com.remote_vitals.backend.user.enums.Gender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.ArrayList;

/**
 * Controller for the signup page.
 * Handles user registration and navigation.
 */
public class SignupController extends BaseController implements Initializable {

    @FXML
    private TextField firstName_input;
    
    @FXML
    private TextField lastName_input;
    
    @FXML
    private TextField email_input;
    
    @FXML
    private PasswordField password_input;
    
    @FXML
    private PasswordField confirm_password_input;
    
    @FXML
    private ChoiceBox<String> user_type_dropdown;
    
    @FXML
    private ChoiceBox<Gender> gender_dropdown;
    
    @FXML
    private TextField phone_number_input;
    
    @FXML
    private ChoiceBox<String> blood_group_dropdown;
    
    @FXML
    private DatePicker date_of_birth_picker;
    
    @FXML
    private TextArea description_input;
    
    @FXML
    private Button signup_button;
    
    @FXML
    private Hyperlink loginLink;
    
    @FXML
    private Label blood_group_label;
    
    @FXML
    private Label date_of_birth_label;

    // Constants for dropdown values
    private static final String USER_TYPE_PATIENT = "Patient";
    private static final String USER_TYPE_DOCTOR = "Doctor";
    private static final String USER_TYPE_ADMIN = "Admin";
    
    /**
     * Initialize the controller. This method is automatically called
     * after the FXML file has been loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        
        try {
            setupUserTypeDropdown();
            setupGenderDropdown();
            setupBloodGroupDropdown();
            setupDatePicker();
            setupValidationListeners();
            setupPatientFieldsVisibility();
        } catch (Exception e) {
            showErrorAlert("Initialization Error", "Error Setting Up Form", 
                    "There was an error initializing the form. Please try again.");
        }
    }

    /**
     * Sets up the user type dropdown with options and listener
     */
    private void setupUserTypeDropdown() {
        if (user_type_dropdown == null) {
            throw new IllegalStateException("User type dropdown is not initialized");
        }

        user_type_dropdown.getItems().clear();
        user_type_dropdown.getItems().addAll(USER_TYPE_PATIENT, USER_TYPE_DOCTOR, USER_TYPE_ADMIN);
        user_type_dropdown.setValue(USER_TYPE_PATIENT);
        
        // Add listener to handle user type changes
        user_type_dropdown.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updatePatientFieldsVisibility();
                
                // Update email prompt based on user type
                switch (newVal) {
                    case USER_TYPE_PATIENT:
                        email_input.setPromptText("patient@example.com");
                        break;
                    case USER_TYPE_DOCTOR:
                        email_input.setPromptText("doctor@example.com");
                        break;
                    case USER_TYPE_ADMIN:
                        email_input.setPromptText("admin@example.com");
                        break;
                }
            }
        });
        
        // Force the prompt text to update initially
        email_input.setPromptText("patient@example.com");
        
        // Add debug listeners
        user_type_dropdown.setOnShowing(event -> {
            System.out.println("User type dropdown is showing");
        });
        
        user_type_dropdown.setOnShown(event -> {
            System.out.println("User type dropdown is shown");
        });
        
        user_type_dropdown.setOnHidden(event -> {
            System.out.println("User type dropdown is hidden");
        });
    }

    /**
     * Sets up the gender dropdown with options
     */
    private void setupGenderDropdown() {
        if (gender_dropdown == null) {
            throw new IllegalStateException("Gender dropdown is not initialized");
        }

        gender_dropdown.getItems().clear();
        gender_dropdown.getItems().addAll(Gender.values());
        gender_dropdown.setValue(Gender.MALE);

        // Add debug listeners
        gender_dropdown.setOnShowing(event -> {
            System.out.println("Gender dropdown is showing");
        });
        
        gender_dropdown.setOnShown(event -> {
            System.out.println("Gender dropdown is shown");
        });
        
        gender_dropdown.setOnHidden(event -> {
            System.out.println("Gender dropdown is hidden");
        });
    }

    /**
     * Sets up the blood group dropdown with options
     */
    private void setupBloodGroupDropdown() {
        if (blood_group_dropdown == null) {
            throw new IllegalStateException("Blood group dropdown is not initialized");
        }

        blood_group_dropdown.getItems().clear();
        blood_group_dropdown.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        blood_group_dropdown.setValue("A+");

        // Add debug listeners
        blood_group_dropdown.setOnShowing(event -> {
            System.out.println("Blood group dropdown is showing");
        });
        
        blood_group_dropdown.setOnShown(event -> {
            System.out.println("Blood group dropdown is shown");
        });
        
        blood_group_dropdown.setOnHidden(event -> {
            System.out.println("Blood group dropdown is hidden");
        });
    }

    /**
     * Sets up the date picker with initial configuration
     */
    private void setupDatePicker() {
        if (date_of_birth_picker == null) {
            throw new IllegalStateException("Date picker is not initialized");
        }

        // Set the date picker to allow dates up to today
        date_of_birth_picker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date != null && date.isAfter(java.time.LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
    }

    /**
     * Sets up validation listeners for password fields
     */
    private void setupValidationListeners() {
        if (password_input == null || confirm_password_input == null) {
            throw new IllegalStateException("Password fields are not initialized");
        }

        password_input.textProperty().addListener((obs, oldVal, newVal) -> {
            validatePassword(newVal);
        });
        
        confirm_password_input.textProperty().addListener((obs, oldVal, newVal) -> {
            validatePasswordsMatch(password_input.getText(), newVal);
        });
    }

    /**
     * Sets up initial visibility of patient-specific fields
     */
    private void setupPatientFieldsVisibility() {
        updatePatientFieldsVisibility();
    }

    /**
     * Updates the visibility of patient-specific fields based on user type
     */
    private void updatePatientFieldsVisibility() {
        if (user_type_dropdown == null || blood_group_label == null || 
            blood_group_dropdown == null || date_of_birth_label == null || 
            date_of_birth_picker == null) {
            throw new IllegalStateException("Required fields are not initialized");
        }

        String userType = user_type_dropdown.getValue();
        boolean isPatient = USER_TYPE_PATIENT.equals(userType);
        boolean isDoctor = USER_TYPE_DOCTOR.equals(userType);
        
        // Update blood group field visibility (Patient only)
        blood_group_label.setVisible(isPatient);
        blood_group_label.setManaged(isPatient);
        blood_group_dropdown.setVisible(isPatient);
        blood_group_dropdown.setManaged(isPatient);
        
        // Update date of birth field visibility (Patient only)
        date_of_birth_label.setVisible(isPatient);
        date_of_birth_label.setManaged(isPatient);
        date_of_birth_picker.setVisible(isPatient);
        date_of_birth_picker.setManaged(isPatient);
        
        // Update description field prompt based on user type
        if (isDoctor) {
            description_input.setPromptText("Enter your professional description");
        } else if (isPatient) {
            description_input.setPromptText("Enter any medical conditions or relevant information");
        } else {
            description_input.setPromptText("Enter any additional information");
        }
    }
    
    /**
     * Validates the password strength.
     * 
     * @param password The password to validate
     * @return True if the password is valid
     */
    private boolean validatePassword(String password) {
        // Basic password validation
        if (password.length() < 8) {
            return false;
        }
        return true;
    }
    
    /**
     * Validates that the passwords match.
     * 
     * @param password The original password
     * @param confirmPassword The confirmed password
     * @return True if the passwords match
     */
    private boolean validatePasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
    
    /**
     * Validates phone number format.
     * 
     * @param phoneNumber The phone number to validate
     * @return True if the phone number is valid
     */
    private boolean validatePhoneNumber(String phoneNumber) {
        // Basic phone number validation (can be enhanced based on requirements)
        return phoneNumber.matches("\\d{10,15}");
    }

    /**
     * Validates that all required dropdowns have values selected
     * 
     * @return True if all required dropdowns have values
     */
    private boolean validateDropdowns() {
        if (user_type_dropdown.getValue() == null) {
            showErrorAlert("Validation Error", "Missing Information", 
                    "Please select a user type.");
            return false;
        }

        if (gender_dropdown.getValue() == null) {
            showErrorAlert("Validation Error", "Missing Information", 
                    "Please select your gender.");
            return false;
        }

        if (USER_TYPE_PATIENT.equals(user_type_dropdown.getValue())) {
            if (blood_group_dropdown.getValue() == null) {
                showErrorAlert("Validation Error", "Missing Information", 
                        "Please select your blood group.");
                return false;
            }
        }

        return true;
    }
    
    /**
     * Handles the signup button click event.
     * Registers the user and navigates to the appropriate dashboard.
     * 
     * @param event The action event
     */
    @FXML
    private void handleSignup(ActionEvent event) {
        // Get input values
        String firstName = firstName_input.getText().trim();
        String lastName = lastName_input.getText().trim();
        String email = email_input.getText().trim();
        String password = password_input.getText().trim();
        String confirmPassword = confirm_password_input.getText().trim();
        String userType = user_type_dropdown.getValue();
        Gender gender = gender_dropdown.getValue();
        String phoneNumber = phone_number_input.getText().trim();
 
        
        // Input validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()) {
            showErrorAlert("Signup Error", "Missing Information", 
                    "Please fill in all required fields.");
            return;
        }
        
        // Validate dropdowns
        if (!validateDropdowns()) {
            return;
        }
        
        // Email validation
        if (!email.contains("@")) {
            showErrorAlert("Signup Error", "Invalid Email", 
                    "Please enter a valid email address.");
            return;
        }
        
        // Phone number validation
        if (!validatePhoneNumber(phoneNumber)) {
            showErrorAlert("Signup Error", "Invalid Phone Number", 
                    "Please enter a valid phone number (10-15 digits).");
            return;
        }
        
        // Password validation
        if (!validatePassword(password)) {
            showErrorAlert("Signup Error", "Weak Password", 
                    "Password must be at least 8 characters long.");
            return;
        }
        
        // Confirm password match
        if (!validatePasswordsMatch(password, confirmPassword)) {
            showErrorAlert("Signup Error", "Password Mismatch", 
                    "Passwords do not match. Please try again.");
            return;
        }
        
        // Additional validation for patients
        if (USER_TYPE_PATIENT.equals(userType)) {
            if (date_of_birth_picker.getValue() == null) {
                showErrorAlert("Signup Error", "Missing Information", 
                        "Please select your date of birth.");
                return;
            }
        }
        
        // Create user based on type
        try {
            if (USER_TYPE_DOCTOR.equals(userType)) {
                // Create doctor with empty lists for appointments, qualifications, and checkups
                // In a real application, this would be handled by a service
                Doctor doc = new Doctor(firstName , lastName , gender , phoneNumber, email , password);
                BaseController.getDb().registerDoctor(doc);

            } else if (USER_TYPE_PATIENT.equals(userType)) {
                LocalDateTime dateOfBirth = date_of_birth_picker.getValue().atStartOfDay();
                Patient pat = new Patient(
                    firstName,
                    lastName,
                    gender,
                    phoneNumber,
                    email,
                    password,
                    "",
                    blood_group_dropdown.getValue(),
                    dateOfBirth
                );
                BaseController.getDb().registerPatient(pat);
                showInfoAlert("Registration Success", "Patient Account Created", 
                        "Your patient account has been created successfully. Please log in with your credentials.");

            } else {
                // Create admin
                showInfoAlert("Registration Success", "Admin Account Created", 
                        "Your admin account has been created successfully. Please log in with your credentials.");
            }
            
            // Navigate to login page
            navigateTo(event, ScreenPaths.LOGIN_PAGE, ScreenPaths.TITLE_LOGIN);
        } catch (Exception e) {
            showErrorAlert("Registration Error", "Account Creation Failed", 
                    "There was an error creating your account. Please try again.");
        }
    }
    
    /**
     * Handles the back button click event.
     * Navigates back to the login page.
     * 
     * @param event The action event
     */
    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, ScreenPaths.LOGIN_PAGE, ScreenPaths.TITLE_LOGIN);
    }
} 