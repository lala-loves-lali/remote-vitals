package com.remote_vitals.user.repositories;

import com.remote_vitals.user.entities.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Integer> {
    List<Qualification> findAllByDoctor_Id(Integer doctorId);
}
