/*
 * Author  : Taimoor Ashraf
 * Purpose : WBC Class that is a sort of vital record
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
@DiscriminatorValue("WBC")
public class WBC extends VitalRecord {
    /******************** Attributes *******************/
    // static constants
    public static final int MIN_WBC = 4; // thousand cells per microliter
    public static final int MAX_WBC = 11; // thousand cells per microliter

    /********************* Setters ********************/
    @Override
    public void setValue(float wbcCount) throws IllegalArgumentException {
        if (!isValidWBC(wbcCount)) {
            throw new IllegalArgumentException(
                "WBC Count (" + wbcCount + ") is outside the valid range of " + MIN_WBC + " to " + MAX_WBC + "."
            );
        }
        super.setValue(wbcCount);
    }

    /******************* Constructors *******************/
    public WBC(Long id, float value) {
        super(id);
        setValue(value);
    }

    public WBC(float value) {
        setValue(value);
    }

    /********************* Methods *********************/
    public static boolean isValidWBC(float wbcCount) {
        return wbcCount >= MIN_WBC && wbcCount <= MAX_WBC;
    }
}
// ----------------------- // END // ---------------------- //
