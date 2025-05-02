package com.remote_vitals.backend.chat.repositories;

import com.remote_vitals.backend.chat.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
