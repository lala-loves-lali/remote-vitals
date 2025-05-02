package com.remote_vitals.backend.entities;

import com.remote_vitals.backend.user.entities.Doctor;
import com.remote_vitals.backend.user.entities.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class CheckUp {
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CheckupGenerator")
    @TableGenerator(
            name = "CheckupGenerator",
            table = "id_generator",
            pkColumnName = "table_of_pk",
            valueColumnName = "value",
            pkColumnValue = "checkup",
            allocationSize = 10
    )
    private Integer id;
    @Column(columnDefinition = "text")
    private String prescription;
    @Column(columnDefinition = "text")
    private String feedback;
    @Column(nullable = false)
    private LocalDateTime timeWhenMade;
    // Relationships
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
