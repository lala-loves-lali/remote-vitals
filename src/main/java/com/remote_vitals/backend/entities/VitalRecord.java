/*
 * Author  : Taimoor Ashraf
 * Purpose : VitalRecord Class That is the super class of all types of 
 *           vitals
 */
// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.backend.entities;

// imports
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

// lombok annotations
@Data
@NoArgsConstructor
@SuperBuilder

// JPA annotations
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vital_type", discriminatorType = DiscriminatorType.STRING)
public class VitalRecord {
    /******************* Attributes *******************/
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "VitalRecordGenerator")
    @TableGenerator(
            name = "VitalRecordGenerator",
            table = "id_generator",
            pkColumnName = "table_of_pk",
            valueColumnName = "value",
            pkColumnValue = "vital_record",
            allocationSize = 10
    )
    private Long id;
    private float value;
    /***************** Relationships ******************/
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL,optional = false)
    @JoinColumn(name = "vital_report_id",nullable = false, updatable = false)
    private VitalReport vitalReport;
    /****************** Constructors ******************/
    public VitalRecord(Long id){ this.id = id;}
}
// ----------------------- // END // ---------------------- //
