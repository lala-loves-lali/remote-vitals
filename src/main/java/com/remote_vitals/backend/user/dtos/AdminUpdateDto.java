package com.remote_vitals.backend.user.dtos;

import com.remote_vitals.backend.user.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AdminUpdateDto extends UserUpdateDto {
    // if we ever add attributes to admin


    public AdminUpdateDto(
            String phoneNumber,
            Visibility pnVisibility,
            String email,
            Visibility eVisibility,
            String password
    ) {
        super(phoneNumber, pnVisibility, email, eVisibility, password);
    }
}
