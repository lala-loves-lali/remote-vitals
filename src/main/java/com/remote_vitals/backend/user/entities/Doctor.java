// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.user.entities;

// imports
import com.remote_vitals.appointment.entities.Appointment;
import com.remote_vitals.checkup.entities.CheckUp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
}
