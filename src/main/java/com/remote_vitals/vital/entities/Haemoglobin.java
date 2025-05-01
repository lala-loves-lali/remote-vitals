/*
 * Author  : Taimoor Ashraf
 * Purpose : Haemoglobin Class that is a sort of vital record
 */
// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.vital.entities;

// imports
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

// lombok annotations
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder

// JPA annotations
@Entity
@DiscriminatorValue("Haemoglobin")
public class Haemoglobin extends VitalRecord {
    /******************** Attributes *******************/
    // static constants
    public static final float MIN_HEMOGLOBIN = 12.0f; // in g/dL
    public static final float MAX_HEMOGLOBIN = 18.0f; // in g/dL

    /********************* Setters ********************/
    @Override
    public void setValue(float haemoglobin) throws IllegalArgumentException {
        if (!isValidHaemoglobin(haemoglobin)) {
            throw new IllegalArgumentException(
                "Haemoglobin (" + haemoglobin + ") is outside the valid range of " + MIN_HEMOGLOBIN + " to " + MAX_HEMOGLOBIN + "."
            );
        }
        super.setValue(haemoglobin);
    }

    /******************* Constructors *******************/
    public Haemoglobin(Long id, float value) {
        super(id);
        setValue(value);
    }

    public Haemoglobin(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    public static boolean isValidHaemoglobin(float haemoglobin) {
        return haemoglobin >= MIN_HEMOGLOBIN && haemoglobin <= MAX_HEMOGLOBIN;
    }
}
// ----------------------- // END // ---------------------- //
