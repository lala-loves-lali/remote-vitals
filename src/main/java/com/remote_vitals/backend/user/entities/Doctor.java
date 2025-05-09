// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.backend.user.entities;

// imports
import java.util.ArrayList;
import java.util.List;

import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.backend.checkup.entities.CheckUp;
import com.remote_vitals.backend.user.enums.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Doctor entity class that extends the base User class.
 * Represents medical professionals in the system with their specialties and relationships.
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
 * @PrimaryKeyJoinColumn - Specifies the foreign key column for the joined inheritance strategy
 */
@Entity
@PrimaryKeyJoinColumn(name = "user_id", columnDefinition = "int")
public class Doctor extends User {
    /******************** Attributes ********************/
    /** Professional description or biography of the doctor */
    @Column(columnDefinition = "text")
    private String description;

    /** Doctor's qualification as a simple string */
    @Column(name = "qualification", columnDefinition = "text")
    private String qualificationString;

    /** List of appointments associated with this doctor */
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

    /** List of professional qualifications held by the doctor */
    @OneToMany(mappedBy = "doctor")
    private List<Qualification> qualifications;

    /** List of medical checkups performed by the doctor */
    @OneToMany(mappedBy = "doctor")
    private List<CheckUp> checkups;

    /** List of patients assigned to this doctor */
    @OneToMany(mappedBy = "assignedDoctor")
    private List<Patient> assignedPatients;

    /**
     * Constructor for creating a new doctor with basic information
     * Initializes empty lists for appointments, qualifications, and checkups
     */
    public Doctor(String firstName, String lastName, Gender gender, String phoneNumber, String email, String password) {
        super(firstName, lastName, gender, phoneNumber, email, password);
        this.description = "";
        this.qualificationString = "";
        this.appointments = new ArrayList<>();
        this.qualifications = new ArrayList<>();
        this.checkups = new ArrayList<>();
        this.assignedPatients = new ArrayList<>();
    }

    /**
     * Constructor with qualification string
     */
    public Doctor(String firstName, String lastName, Gender gender, String phoneNumber, String email, String password, String qualification) {
        super(firstName, lastName, gender, phoneNumber, email, password);
        this.description = "";
        this.qualificationString = qualification;
        this.appointments = new ArrayList<>();
        this.qualifications = new ArrayList<>();
        this.checkups = new ArrayList<>();
        this.assignedPatients = new ArrayList<>();
    }
}
