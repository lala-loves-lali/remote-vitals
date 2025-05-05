package com.remote_vitals.backend.vitalReport.entities;

// imports
import com.remote_vitals.backend.user.entities.Patient;
import com.remote_vitals.backend.vital.entities.VitalRecord;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

// lombok annotations
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

// JPA annotations
@Entity
public class VitalReport {
    /******************** Attributes ********************/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "VitalReportGenerator")
    @TableGenerator(
            name = "VitalReportGenerator",
            table = "id_generator",
            pkColumnName = "table_of_pk",
            valueColumnName = "value",
            pkColumnValue = "vital_report",
            allocationSize = 10
    )
    private Integer id;
    @Column(name = "report_when_made",nullable = false)
    private LocalDateTime reportWhenMade;
    /******************* Relationships *******************/
    @OneToMany(mappedBy = "vitalReport")
    List<VitalRecord> vitalRecords;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
