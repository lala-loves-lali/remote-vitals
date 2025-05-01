package com.remote_vitals.chat.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "message_type")
public class Message {
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "MessageGenerator")
    @TableGenerator(
            name = "MessageGenerator",
            table = "id_generator",
            pkColumnName = "table_of_pk",
            valueColumnName = "value",
            pkColumnValue = "message",
            allocationSize = 10
    )
    private Integer id;
    @NotNull
    private LocalDateTime timeSent;
    // RelationShip
    @ManyToOne(cascade = CascadeType.PERSIST,optional = false,fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;
}
