package com.remote_vitals.backend.user.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// JPA annotations
@Entity
public class Qualification {
    /******************** Attributes ********************/
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "QualificationGenerator")
    @TableGenerator(
            name = "QualificationGenerator",
            table = "id_generator",
            pkColumnName = "table_of_pk",
            valueColumnName = "value",
            pkColumnValue = "qualification",
            allocationSize = 10
    )
    private Integer id;
    @Column(nullable = false)
    private String label;
  
    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    public Qualification(String label, Doctor doctor) {
        this.label = label;
        this.doctor = doctor;
    }
}
