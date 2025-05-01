package com.remote_vitals.user.repositories;

import com.remote_vitals.user.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findByEmail(String email);
}
