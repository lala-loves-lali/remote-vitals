package com.remote_vitals.backend.user.repositories;

import com.remote_vitals.backend.user.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

}
