/*
 * Author  : Taimoor Ashraf
 * Purpose : Height Class that is a sort of vital record
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
@DiscriminatorValue("Height")
public class Height extends VitalRecord {
    /******************** Attributes *******************/
    // static
    public static final float MIN_HEIGHT = 0.0f;
    public static final float MAX_HEIGHT = 0.0f;
    /********************* Setters *********************/
    @Override
    public void setValue(float height) throws IllegalArgumentException {
        // check if height is valid
        int result = isValidHeight(height);
        if(result == 0){
            super.setValue(height);
        }else if(result == 1){
            throw new IllegalArgumentException
            ("Height( " + height + " ) is greater than maximum height( "+ MAX_HEIGHT +" )\n");
        }else{
            throw new IllegalArgumentException
            ("Height( " + height + " ) is less than minimum height( "+ MIN_HEIGHT +" )\n");
        }
    }
    /******************* Constructors *******************/
    public Height(Integer id,float value){
        super(id);
        setValue(value);
    }
    public Height(float value){
        setValue(value);
    }
    /********************* Methods *********************/

    // returns 0 if height lies in valid range
    // returns 1 if height is greater than maximum height,
    // returns -1 if height is less than minimum height
    public static int isValidHeight(float height){
        if(height < MIN_HEIGHT){ return -1;}
        else if(height > MAX_HEIGHT){ return 1;}
        else{ return 0;}
    }
}
// ----------------------- // END // ---------------------- //
