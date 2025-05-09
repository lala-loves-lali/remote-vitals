// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.backend.user.entities;

// imports
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.backend.checkup.entities.CheckUp;
import com.remote_vitals.backend.user.enums.Gender;
import com.remote_vitals.backend.vitalReport.entities.VitalReport;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

/**
 * Patient entity class that extends the base User class.
 * Represents patients in the system with their medical information and relationships.
 * 
 * Lombok annotations:
 * @Data - Generates getters, setters, toString, equals, and hashCode
 * @NoArgsConstructor - Generates constructor with no parameters
 * @AllArgsConstructor - Generates constructor with all parameters
 * @EqualsAndHashCode - Generates equals and hashCode methods
 * @SuperBuilder - Enables builder pattern with inheritance support
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder

/**
 * JPA annotations:
 * @Entity - Marks this class as a JPA entity
 * @Table - Specifies the table name for this entity
 * @PrimaryKeyJoinColumn - Specifies the foreign key column for the joined inheritance strategy
 */
@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "user_id", columnDefinition = "int")
public class Patient extends User {
    /******************** Attributes ********************/
    /** Medical description or history of the patient */
    @Column(columnDefinition = "text")
    private String description;

    /** Patient's blood group with validation pattern */
    @Column(name = "blood_group", length = 3)
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Invalid blood group format")
    private String bloodGroup;

    /** Patient's date of birth with validation to ensure it's in the past */
    @Column(name = "date_of_birth", nullable = true)
    @Past(message = "Date of birth must be in the past")
    private LocalDateTime dateOfBirth;

    /** List of appointments scheduled for this patient */
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    /** List of vital reports containing the patient's medical measurements */
    @OneToMany(mappedBy = "patient")
    private List<VitalReport> vitalReport;

    /** List of medical checkups performed on this patient */
    @OneToMany(mappedBy = "patient")
    private List<CheckUp> checkups;

    /** The doctor assigned to this patient */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_doctor_id")
    private Doctor assignedDoctor;

    /**
     * Constructor for creating a new patient with basic information
     * @param firstName First name of the patient
     * @param lastName Last name of the patient
     * @param gender Gender of the patient
     * @param phoneNumber Contact phone number
     * @param email Email address
     * @param password Account password
     * @param description Medical description
     * @param bloodGroup Blood group (must match pattern A/B/AB/O with +/-)
     * @param dateOfBirth Date of birth (must be in the past)
     */
    public Patient(String firstName, String lastName, Gender gender, String phoneNumber, String email, String password, String description, String bloodGroup, LocalDateTime dateOfBirth) {
        super(firstName, lastName, gender, phoneNumber, email, password);
        this.description = description;
        this.bloodGroup = bloodGroup;
        this.dateOfBirth = dateOfBirth;
        this.appointments = new ArrayList<>();
        this.vitalReport = new ArrayList<>();
        this.checkups = new ArrayList<>();
    }
}
