package com.remote_vitals.backend.appointment.repositories;

import com.remote_vitals.backend.appointment.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {
}
