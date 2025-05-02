//package com.remote_vitals;
//
//import com.remote_vitals.user.entities.Patient;
//import com.remote_vitals.user.enums.Gender;
//import com.remote_vitals.user.repositories.PatientRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Test {
//    public PatientRepository patientRepository;
//
//    @Autowired
//    public void PatientRepository(PatientRepository patientRepository){
//        this.patientRepository = patientRepository;
//    }
//
//    public void tester(){
//        Patient patient = new Patient();
//        patient.setFirstName("John");
//        patient.setLastName("Smith");
//        patient.setBloodGroup("B+");
//        patient.setGender(Gender.MALE);
//        patient.setEmail("example@email.com");
//        patient.setPassword("12345");
//        this.patientRepository.save(patient);
//       // System.out.println(this.patientRepository.findByEmail("example@email.com").toString());
//    }
//}
