package com.remote_vitals.backend.services;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    public String analyzePassword(String password) {
        try{
            if(password == null || password.isBlank())
                throw new RuntimeException("Password field is empty");

            if(password.length() < 8 || password.length() > 25)
                throw new RuntimeException("Password length must be between 8 and 25 characters");

            if(!password.matches(".*[a-z].*"))
                throw new RuntimeException("Password must contain at least one lowercase letter");

            if(!password.matches(".*[1234567890].*"))
                throw new RuntimeException("Password must contain at least one digit");
            return password;
        } catch(RuntimeException ex){
            return ex.getMessage();
        }
    }
}
