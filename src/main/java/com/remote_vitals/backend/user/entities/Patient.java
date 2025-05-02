// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.backend.user.entities;

// imports
import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.backend.entities.CheckUp;
import com.remote_vitals.backend.entities.VitalReport;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

// lombok annotations
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder

// JPA annotations
@Entity
@Table(name = "patient")
@PrimaryKeyJoinColumn(name = "user_id", columnDefinition = "bigint")
public class Patient extends User {
    /******************** Attributes ********************/
    @Column(columnDefinition = "text")
    private String description;
    @Column(name = "blood_group", nullable = false, length = 3)
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Invalid blood group format")
    private String bloodGroup;
    @Column(name = "date_of_birth", nullable = true)
    @Past(message = "Date of birth must be in the past")
    private LocalDateTime dateOfBirth;
    // RelationShips
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient")
    private List<VitalReport> vitalReport;

    @OneToMany(mappedBy = "patient")
    private List<CheckUp> checkups;
}
