package com.remote_vitals.backend.chat.entities;

import com.remote_vitals.backend.appointment.entities.Appointment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class ChatRoom {
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ChatRoomGenerator")
    @TableGenerator(
            name = "ChatRoomGenerator",
            table = "id_generator",
            pkColumnName = "table_of_pk",
            valueColumnName = "value",
            pkColumnValue = "chat_room",
            allocationSize = 10
    )
    private Integer id;
    private LocalDateTime timeCreated;
    // Relationships
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages;
}
