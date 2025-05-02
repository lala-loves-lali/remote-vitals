/*
 * Author  : Taimoor Ashraf
 * Purpose : Weight Class that is a sort of vital record
 */
// ---------------------- // BEGIN // --------------------- //
package com.remote_vitals.backend.entities;

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
@DiscriminatorValue("Weight")
public class Weight extends VitalRecord {
    /******************** Attributes *******************/
    // static constants
    public static final float MIN_WEIGHT = 0.0f;
    public static final float MAX_WEIGHT = 500.0f; // Max weight in kg
    
    /********************* Setters ********************/
    @Override
    public void setValue(float weight) throws IllegalArgumentException {
        if (!isValidWeight(weight)) {
            throw new IllegalArgumentException(
                "Weight (" + weight + ") is outside the valid range of " + MIN_WEIGHT + " to " + MAX_WEIGHT + "."
            );
        }
        super.setValue(weight);
    }

    /******************* Constructors *******************/
    public Weight(Long id, float value) {
        super(id);
        setValue(value);
    }

    public Weight(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    public static boolean isValidWeight(float weight) {
        return weight >= MIN_WEIGHT && weight <= MAX_WEIGHT;
    }
}
// ----------------------- // END // ---------------------- //