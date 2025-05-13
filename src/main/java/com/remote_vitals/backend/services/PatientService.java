package com.remote_vitals.backend.services;

import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.entities.User;
import com.remote_vitals.backend.user.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing patient-related operations
 */
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserService userService;

    @Autowired
    public PatientService(PatientRepository patientRepository, @Lazy UserService userService) {
        this.patientRepository = patientRepository;
        this.userService = userService;
    }

    /**
     * Get all patients from the database
     * @return List of all patients
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
    /**
     * Get patients assigned to a specific doctor
     * @param doctorId The ID of the doctor
     * @return List of patients assigned to the doctor
     */
    public List<Patient> getPatientsByDoctor(Integer doctorId) {
        return patientRepository.findByDoctorId(doctorId);
    }
    
    /**
     * Get patients assigned to the currently logged-in doctor
     * @return List of patients assigned to the current doctor, or empty list if not a doctor
     */
    public List<Patient> getPatientsForCurrentDoctor() {
        Optional<User> currentUser = userService.getCurrentUser();
        if (currentUser.isPresent() && currentUser.get() instanceof Doctor) {
            Doctor doctor = (Doctor) currentUser.get();
            return patientRepository.findByDoctorId(doctor.getId());
        }
        return new ArrayList<>();
    }
    
    /**
     * Find a patient by ID
     * @param id The patient ID
     * @return Optional containing the patient if found
     */
    public Optional<Patient> getPatientById(Integer id) {
        return patientRepository.findById(id);
    }
    
    /**
     * Find patient by email
     * @param email The email to search for
     * @return Optional containing the patient if found
     */
    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }
    
    /**
     * Find patients by blood group
     * @param bloodGroup The blood group to search for
     * @return List of patients with the specified blood group
     */
    public List<Patient> getPatientsByBloodGroup(String bloodGroup) {
        return patientRepository.findByBloodGroup(bloodGroup);
    }
    
    /**
     * Search for patients by name
     * @param name The name to search for
     * @return List of patients matching the name search
     */
    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }
} 