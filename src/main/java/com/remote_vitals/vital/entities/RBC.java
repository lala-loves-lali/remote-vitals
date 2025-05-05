/*
 * Author  : Taimoor Ashraf
 * Purpose : RBC Class that is a sort of vital record
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
@DiscriminatorValue("RBC")
public class RBC extends VitalRecord {
    /******************** Attributes *******************/
    // static constants
    public static final int MIN_RBC = 4; // million cells per microliter
    public static final int MAX_RBC = 6; // million cells per microliter

    /********************* Setters ********************/
    @Override
    public void setValue(float rbcCount) throws IllegalArgumentException {
        if (!isValidRBC(rbcCount)) {
            throw new IllegalArgumentException(
                "RBC Count (" + rbcCount + ") is outside the valid range of " + MIN_RBC + " to " + MAX_RBC + "."
            );
        }
        super.setValue(rbcCount);
    }

    /******************* Constructors *******************/
    public RBC(Integer id, float value) {
        super(id);
        setValue(value);
    }

    public RBC(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    public static boolean isValidRBC(float rbcCount) {
        return rbcCount >= MIN_RBC && rbcCount <= MAX_RBC;
    }
}
// ----------------------- // END // ---------------------- //
