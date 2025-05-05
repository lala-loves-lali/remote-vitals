package com.remote_vitals.backend.user.entities;

import com.remote_vitals.backend.user.enums.Gender;
import com.remote_vitals.backend.user.enums.Visibility;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

// Lombok annotations
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

// JPA annotations
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    /******************** Attributes ********************/
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "UserGenerator")
    @TableGenerator(
            name = "UserGenerator",
            table = "id_generator",
            pkColumnName = "table_of_pk",
            valueColumnName = "value",
            pkColumnValue = "user",
            allocationSize = 10
    )
    private Integer id;

    private String profilePhoto; // address of photo

    @Column(nullable = false)
    private String firstName;

    public User(String firstName, String lastName, Gender gender, String phoneNumber, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.pnVisibility = Visibility.PRIVATE;
        this.email = email;
        this.eVisibility = Visibility.PRIVATE;
        this.password = password;
    }

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Visibility pnVisibility;

    @Email
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    private Visibility eVisibility;

    @Column(nullable = false)
    private String password;

}
