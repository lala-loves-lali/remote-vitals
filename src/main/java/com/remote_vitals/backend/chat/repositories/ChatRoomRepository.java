package com.remote_vitals.backend.chat.repositories;

import com.remote_vitals.backend.chat.entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
}
