package com.remote_vitals.backend.user.entities;

import jakarta.persistence.*;
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
    private Long id;
    @Column(nullable = false)
    private String label;
    @Column(columnDefinition = "text", nullable = false)
    private String description;
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST,optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
}
