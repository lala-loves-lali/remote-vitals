package com.remote_vitals.backend.services;

import com.remote_vitals.backend.db_handler.StaticDataClass;
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.user.enums.Gender;
import com.remote_vitals.backend.user.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PanicButtonService {
    // Bean
    private final PatientRepository patientRepository;
    private final EmailService emailService;
    // Constructor
    public PanicButtonService(
            PatientRepository patientRepository,
            EmailService emailService
    ) {
        this.patientRepository = patientRepository;
        this.emailService = emailService;
    }
    // Method
    public String panicButton() {
        Optional<Patient> wrappedPatient = patientRepository.findById(StaticDataClass.currentUserId);
        if(wrappedPatient.isEmpty()) return "Patient Not Found";
        Patient patient = wrappedPatient.get();
        if(patient.getNextOfKinEmail() == null || patient.getNextOfKinEmail().isBlank())
            return "Next Of Kin Not Found";
        if(
            emailService.sendEmail(
                    patient.getNextOfKinEmail(),
                    patient.getFirstName() + " " + patient.getLastName()
                    +" Health Emergency Alert",
                    "Your beloved " + patient.getFirstName() + " " + patient.getLastName() +
                    " is not feeling well, " + (
                            patient.getGender() == Gender.FEMALE ? "she": "he"
                    ) +
                    " has pressed the panic button to inform you about this emergency. Kindly approach" +
                    (patient.getGender() == Gender.FEMALE ? "her":"him") + " immediately!!!",
                    null
            ).equals("Email Sent Successfully")
        ){
            return "Panic Button Pressed Successfully";
        }
        else return "Email was not sent due to unknown reasons";
    }
}
