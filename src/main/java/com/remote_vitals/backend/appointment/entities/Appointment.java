package com.remote_vitals.backend.appointment.entities;

import com.remote_vitals.backend.appointment.enums.AppointmentStatus;
//import com.remote_vitals.backend.chat.entities.ChatRoom;
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
    @Column(name = "link_for_room")
    private String linkForRoom;
    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;


    public Appointment(AppointmentStatus status, Patient patient, Doctor doctor, Schedule schedule, String linkForRoom/*, ChatRoom chatRoom */) {
        this.status = status;
        this.patient = patient;
        this.doctor = doctor;
        this.schedule = schedule;
        this.linkForRoom = linkForRoom;
//        this.chatRoom = chatRoom;
    }


    public Appointment(Patient patient, Doctor doctor, Schedule schedule, String linkForRoom /*, ChatRoom chatRoom */) {
        this.status=AppointmentStatus.REQUESTED;
        this.patient = patient;
        this.doctor = doctor;
        this.schedule = schedule;
        this.linkForRoom = linkForRoom;
//        this.chatRoom = chatRoom;
    }
}

