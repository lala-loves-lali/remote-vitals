package com.remote_vitals.chat.repositories;

import com.remote_vitals.chat.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
