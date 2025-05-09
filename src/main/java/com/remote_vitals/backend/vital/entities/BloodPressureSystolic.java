/*
 * Author  : Taimoor Ashraf
 * Purpose : BloodPressureSystolic Class that is a sort of vital record
 */
// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.backend.vital.entities;

// imports
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Represents a systolic blood pressure measurement.
 * Extends VitalRecord and includes validation for normal blood pressure ranges.
 * 
 * Lombok annotations:
 * @Data - Generates getters, setters, toString, equals, and hashCode
 * @NoArgsConstructor - Generates constructor with no parameters
 * @EqualsAndHashCode - Generates equals and hashCode methods
 * @SuperBuilder - Enables builder pattern with inheritance support
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder

/**
 * JPA annotations:
 * @Entity - Marks this class as a JPA entity
 * @DiscriminatorValue - Specifies the value used in the discriminator column for this type
 */
@Entity
@DiscriminatorValue("BloodPressureSystolic")
public class BloodPressureSystolic extends VitalRecord {
    /******************** Attributes *******************/
    /** Minimum normal systolic blood pressure value (90 mmHg) */
    public static final int MIN_SYSTOLIC = 90;
    
    /** Maximum normal systolic blood pressure value (180 mmHg) */
    public static final int MAX_SYSTOLIC = 180;

    /********************* Setters ********************/
    /**
     * Sets the systolic blood pressure value with validation
     * @param systolic The systolic blood pressure value to set
     * @throws IllegalArgumentException if the value is outside the normal range
     */
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
    /**
     * Constructor that takes both ID and value
     * @param id The ID for this record
     * @param value The systolic blood pressure value
     */
    public BloodPressureSystolic(Integer id, float value) {
        super(id);
        setValue(value);
    }

    /**
     * Constructor that takes only the value
     * @param value The systolic blood pressure value
     */
    public BloodPressureSystolic(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    /**
     * Validates if a systolic blood pressure value is within normal range
     * @param systolic The value to validate
     * @return true if the value is within normal range, false otherwise
     */
    public static boolean isValidSystolic(float systolic) {
        return systolic >= MIN_SYSTOLIC && systolic <= MAX_SYSTOLIC;
    }

    
}
