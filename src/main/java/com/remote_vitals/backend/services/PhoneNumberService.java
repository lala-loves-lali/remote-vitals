package com.remote_vitals.backend.services;

import org.springframework.stereotype.Service;

@Service
public class PhoneNumberService {
    public String analyzePhoneNumber(String phoneNumber){
        try{
            if(phoneNumber == null || phoneNumber.isBlank())
                throw new RuntimeException("Phone number field is empty");

            if(!phoneNumber.matches("^(\\+\\d{1,3}[- ]?)?(?:\\d{3}[- ]?\\d{7}|03\\d{9})$"))
                throw new RuntimeException("Invalid phone number format");

            return phoneNumber;

        }catch (RuntimeException ex){
            return ex.getMessage();
        }
    }
}
