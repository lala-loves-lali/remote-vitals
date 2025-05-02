// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.backend.user.entities;

// imports
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

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
