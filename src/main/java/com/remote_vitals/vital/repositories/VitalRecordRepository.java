package com.remote_vitals.vital.repositories;

import com.remote_vitals.vital.entities.VitalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VitalRecordRepository extends JpaRepository<VitalRecord, Integer> {
}
