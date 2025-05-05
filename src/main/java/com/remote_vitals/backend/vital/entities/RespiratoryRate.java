/*
 * Author  : Taimoor Ashraf
 * Purpose : RespiratoryRate Class that is a sort of vital record
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
@DiscriminatorValue("RespiratoryRate")
public class RespiratoryRate extends VitalRecord {
    /******************** Attributes *******************/
    // static constants
    public static final int MIN_RESPIRATORY_RATE = 12; // breaths per minute
    public static final int MAX_RESPIRATORY_RATE = 20; // breaths per minute

    /********************* Setters ********************/
    @Override
    public void setValue(float respiratoryRate) throws IllegalArgumentException {
        if (!isValidRespiratoryRate(respiratoryRate)) {
            throw new IllegalArgumentException(
                "Respiratory Rate (" + respiratoryRate + ") is outside the valid range of " + MIN_RESPIRATORY_RATE + " to " + MAX_RESPIRATORY_RATE + "."
            );
        }
        super.setValue(respiratoryRate);
    }

    /******************* Constructors *******************/
    public RespiratoryRate(Integer id, float value) {
        super(id);
        setValue(value);
    }

    public RespiratoryRate(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    public static boolean isValidRespiratoryRate(float respiratoryRate) {
        return respiratoryRate >= MIN_RESPIRATORY_RATE && respiratoryRate <= MAX_RESPIRATORY_RATE;
    }
}
// ----------------------- // END // ---------------------- //
