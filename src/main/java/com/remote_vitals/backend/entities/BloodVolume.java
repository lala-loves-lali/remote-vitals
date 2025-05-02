/*
 * Author  : Taimoor Ashraf
 * Purpose : BloodVolume Class that is a sort of vital record
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
@DiscriminatorValue("BloodVolume")
public class BloodVolume extends VitalRecord {
    /******************** Attributes *******************/
    // static constants
    public static final float MIN_BLOOD_VOLUME = 200.0f; // in mL
    public static final float MAX_BLOOD_VOLUME = 7000.0f; // in mL

    /********************* Setters ********************/
    @Override
    public void setValue(float volume) throws IllegalArgumentException {
        if (!isValidBloodVolume(volume)) {
            throw new IllegalArgumentException(
                "Blood Volume (" + volume + ") is outside the valid range of " + MIN_BLOOD_VOLUME + " to " + MAX_BLOOD_VOLUME + "."
            );
        }
        super.setValue(volume);
    }

    /******************* Constructors *******************/
    public BloodVolume(Long id, float value) {
        super(id);
        setValue(value);
    }

    public BloodVolume(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    public static boolean isValidBloodVolume(float volume) {
        return volume >= MIN_BLOOD_VOLUME && volume <= MAX_BLOOD_VOLUME;
    }
}
// ----------------------- // END // ---------------------- //
