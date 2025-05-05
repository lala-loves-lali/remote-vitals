/*
 * Author  : Taimoor Ashraf
 * Purpose : BloodPressureSystolic Class that is a sort of vital record
 */
// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.backend.vital.entities;

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
@DiscriminatorValue("BloodPressureSystolic")
public class BloodPressureSystolic extends VitalRecord {
    /******************** Attributes *******************/
    // static constants
    public static final int MIN_SYSTOLIC = 90; // Normal lower bound
    public static final int MAX_SYSTOLIC = 180; // Normal upper bound

    /********************* Setters ********************/
    @Override
    public void setValue(float systolic) throws IllegalArgumentException {
        if (!isValidSystolic(systolic)) {
            throw new IllegalArgumentException(
                "Systolic Blood Pressure (" + systolic + ") is outside the valid range of " + MIN_SYSTOLIC + " to " + MAX_SYSTOLIC + "."
            );
        }
        super.setValue(systolic);
    }

    /******************* Constructors *******************/
    public BloodPressureSystolic(Integer id, float value) {
        super(id);
        setValue(value);
    }

    public BloodPressureSystolic(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    public static boolean isValidSystolic(float systolic) {
        return systolic >= MIN_SYSTOLIC && systolic <= MAX_SYSTOLIC;
    }
}
