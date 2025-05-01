package com.remote_vitals.chat.entities;

import com.remote_vitals.appointment.entities.Appointment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    // Relationships
    @OneToOne(cascade = CascadeType.PERSIST, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", unique = true, nullable = false)
    private Appointment appointment;
}
