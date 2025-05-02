/*
 * Author  : Taimoor Ashraf
 * Purpose : PlateletCount Class that is a sort of vital record
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
@DiscriminatorValue("PlateletCount")
public class PlateletCount extends VitalRecord {
    /******************** Attributes *******************/
    // static constants
    public static final int MIN_PLATELET_COUNT = 150000; // per microliter
    public static final int MAX_PLATELET_COUNT = 450000; // per microliter

    /********************* Setters ********************/
    @Override
    public void setValue(float plateletCount) throws IllegalArgumentException {
        if (!isValidPlateletCount(plateletCount)) {
            throw new IllegalArgumentException(
                "Platelet Count (" + plateletCount + ") is outside the valid range of " + MIN_PLATELET_COUNT + " to " + MAX_PLATELET_COUNT + "."
            );
        }
        super.setValue(plateletCount);
    }

    /******************* Constructors *******************/
    public PlateletCount(Long id, float value) {
        super(id);
        setValue(value);
    }

    public PlateletCount(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    public static boolean isValidPlateletCount(float plateletCount) {
        return plateletCount >= MIN_PLATELET_COUNT && plateletCount <= MAX_PLATELET_COUNT;
    }
}
// ----------------------- // END // ---------------------- //
