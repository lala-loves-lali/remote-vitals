// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.user.entities;

// imports
import com.remote_vitals.appointment.entities.Appointment;
import com.remote_vitals.vitalReport.entities.VitalReport;
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
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder

// JPA annotations
@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "user_id", columnDefinition = "bigint")
public class Admin extends User{

}
