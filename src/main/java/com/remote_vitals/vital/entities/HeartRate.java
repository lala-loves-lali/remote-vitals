/*
 * Author  : Taimoor Ashraf
 * Purpose : HeartRate Class that is a sort of vital record
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
@DiscriminatorValue("HeartRate")
public class HeartRate extends VitalRecord {
    /******************** Attributes *******************/
    // static constants
    public static final int MIN_HEART_RATE = 60; // beats per minute
    public static final int MAX_HEART_RATE = 100; // beats per minute

    /********************* Setters ********************/
    @Override
    public void setValue(float heartRate) throws IllegalArgumentException {
        if (!isValidHeartRate(heartRate)) {
            throw new IllegalArgumentException(
                "Heart Rate (" + heartRate + ") is outside the valid range of " + MIN_HEART_RATE + " to " + MAX_HEART_RATE + "."
            );
        }
        super.setValue(heartRate);
    }

    /******************* Constructors *******************/
    public HeartRate(Integer id, float value) {
        super(id);
        setValue(value);
    }

    public HeartRate(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    public static boolean isValidHeartRate(float heartRate) {
        return heartRate >= MIN_HEART_RATE && heartRate <= MAX_HEART_RATE;
    }
}
// ----------------------- // END // ---------------------- //
