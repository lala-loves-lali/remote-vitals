package com.remote_vitals.vitalReport.repositories;

import com.remote_vitals.vitalReport.entities.VitalReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VitalReportRepository extends JpaRepository<VitalReport, Integer> {
}
