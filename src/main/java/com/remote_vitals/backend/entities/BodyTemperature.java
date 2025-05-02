/*
 * Author  : Taimoor Ashraf
 * Purpose : BodyTemperature Class that is a sort of vital record
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
@DiscriminatorValue("BodyTemperature")
public class BodyTemperature extends VitalRecord {
    /******************** Attributes *******************/
    // static constants
    public static final float MIN_TEMP = 30.0f; // Hypothermia threshold
    public static final float MAX_TEMP = 42.0f; // Hyperthermia threshold

    /********************* Setters ********************/
    @Override
    public void setValue(float temperature) throws IllegalArgumentException {
        if (!isValidTemperature(temperature)) {
            throw new IllegalArgumentException(
                "Temperature (" + temperature + ") is outside the valid range of " + MIN_TEMP + " to " + MAX_TEMP + "."
            );
        }
        super.setValue(temperature);
    }

    /******************* Constructors *******************/
    public BodyTemperature(Long id, float value) {
        super(id);
        setValue(value);
    }

    public BodyTemperature(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    public static boolean isValidTemperature(float temperature) {
        return temperature >= MIN_TEMP && temperature <= MAX_TEMP;
    }
}
// ----------------------- // END // ---------------------- //
