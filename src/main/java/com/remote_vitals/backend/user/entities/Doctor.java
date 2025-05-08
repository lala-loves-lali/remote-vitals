// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.backend.user.entities;

// imports
import com.remote_vitals.backend.appointment.entities.Appointment;
import com.remote_vitals.backend.checkup.entities.CheckUp;
import com.remote_vitals.backend.user.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

// lombok annotations
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder

// JPA annotations
@Entity
@PrimaryKeyJoinColumn(name = "user_id", columnDefinition = "bigint")
public class Doctor extends User {
    /******************** Attributes ********************/
    @Column(columnDefinition = "text")
    private String description;
    // RelationShips
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor")
    private List<Qualification> qualifications;

    @OneToMany(mappedBy = "doctor")
    private List<CheckUp> checkups;


    public Doctor(String firstName, String lastName, Gender gender, String phoneNumber, String email, String password) {
        super(firstName, lastName, gender, phoneNumber, email, password);
        this.description = "";
        this.appointments = new ArrayList<>();
        this.qualifications = new ArrayList<>();
        this.checkups = new ArrayList<>();
    }
}
