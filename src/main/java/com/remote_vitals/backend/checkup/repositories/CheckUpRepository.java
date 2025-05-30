package com.remote_vitals.backend.checkup.repositories;

import com.remote_vitals.backend.checkup.entities.CheckUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckUpRepository extends JpaRepository<CheckUp, Integer> {
}
