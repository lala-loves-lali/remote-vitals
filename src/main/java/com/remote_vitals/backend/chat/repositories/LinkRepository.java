package com.remote_vitals.backend.chat.repositories;

import com.remote_vitals.backend.chat.entities.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Integer> {
}
