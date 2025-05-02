package com.remote_vitals.backend.appointment.entities;

import com.remote_vitals.backend.appointment.enums.AppointmentStatus;
import com.remote_vitals.backend.chat.entities.ChatRoom;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
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
public class Appointment {
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AppointmentGenerator")
    @TableGenerator(
            name = "AppointmentGenerator",
            table = "id_generator",
            pkColumnName = "table_of_pk",
            valueColumnName = "value",
            pkColumnValue = "appointment",
            allocationSize = 10
    )
    private Integer id;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    // Relationships
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            optional = false
    )
    @JoinColumn(
            name = "patient_id",
            nullable = false,
            updatable = false
    )
    private Patient patient;
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            optional = false
    )
    @JoinColumn(
            name = "doctor_id",
            nullable = false,
            updatable = false
    )
    private Doctor doctor;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(
            name = "schedule_id"
    )
    private Schedule schedule;

    @OneToOne(mappedBy = "appointment")
    private ChatRoom chatRoom;

}

