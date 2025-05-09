package com.remote_vitals.backend.user.repositories;

import com.remote_vitals.backend.user.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
