/*
 * Author  : Taimoor Ashraf
 * Purpose : BloodPressureDiastolic Class that is a sort of vital record
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
@DiscriminatorValue("BloodPressureDiastolic")
public class BloodPressureDiastolic extends VitalRecord {
    /******************** Attributes *******************/
    // static constants
    public static final int MIN_DIASTOLIC = 60; // Normal lower bound
    public static final int MAX_DIASTOLIC = 120; // Normal upper bound

    /********************* Setters ********************/
    @Override
    public void setValue(float diastolic) throws IllegalArgumentException {
        if (!isValidDiastolic(diastolic)) {
            throw new IllegalArgumentException(
                "Diastolic Blood Pressure (" + diastolic + ") is outside the valid range of " + MIN_DIASTOLIC + " to " + MAX_DIASTOLIC + "."
            );
        }
        super.setValue(diastolic);
    }

    /******************* Constructors *******************/
    public BloodPressureDiastolic(Long id, float value) {
        super(id);
        setValue(value);
    }

    public BloodPressureDiastolic(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    public static boolean isValidDiastolic(float diastolic) {
        return diastolic >= MIN_DIASTOLIC && diastolic <= MAX_DIASTOLIC;
    }
}
