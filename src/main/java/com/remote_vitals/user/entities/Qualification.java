package com.remote_vitals.user.entities;

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
    private Integer id;
    @Column(nullable = false)
    private String label;
    @Column(columnDefinition = "text", nullable = false)
    private String description;
    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
