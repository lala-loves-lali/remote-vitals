package com.remote_vitals.backend.appointment.entities;

import com.remote_vitals.backend.appointment.enums.AppointmentStatus;
//import com.remote_vitals.backend.chat.entities.ChatRoom;
import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Check(name = "appointment_time_check", constraints = "starting_time < ending_time")
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
    
    // Schedule fields integrated directly into appointment
    @NotNull
    private LocalDateTime startingTime;

    @NotNull
    private LocalDateTime endingTime;
    
    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // Constructors with integrated schedule fields
    public Appointment(AppointmentStatus status, Patient patient, Doctor doctor, 
                       LocalDateTime startingTime, LocalDateTime endingTime, String linkForRoom) {
        this.status = status;
        this.patient = patient;
        this.doctor = doctor;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.linkForRoom = linkForRoom;
    }

    public Appointment(Patient patient, Doctor doctor, 
                       LocalDateTime startingTime, LocalDateTime endingTime, String linkForRoom) {
        this.status = AppointmentStatus.REQUESTED;
        this.patient = patient;
        this.doctor = doctor;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.linkForRoom = linkForRoom;
    }
    
    /**
     * Helper method to create an appointment with a default 1-hour duration
     */
    public static Appointment createWithHourDuration(Patient patient, Doctor doctor, 
                                                    LocalDateTime startTime, String linkForRoom) {
        return new Appointment(
            AppointmentStatus.REQUESTED, 
            patient, 
            doctor, 
            startTime, 
            startTime.plusHours(1), 
            linkForRoom
        );
    }
}

