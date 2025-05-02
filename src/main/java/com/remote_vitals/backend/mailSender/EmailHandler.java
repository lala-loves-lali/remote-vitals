package com.remote_vitals.mailSender;



public class EmailHandler {

    public static void sendPasswordSetMail(String email, String password) {
        String message = "Your password is: " + password;

        EmailTemplate.sendEmail(email,"Reset Password Of your RHMS",message);
    }

}
