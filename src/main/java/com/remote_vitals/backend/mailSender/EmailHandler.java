package com.remote_vitals.backend.mailSender;



public class EmailHandler {

    public static void sendPasswordSetMail(String email, String password) {
        String message = "Your password is: " + password;

        EmailTemplate.sendEmail(email,"Reset Password Of your RHMS",message);
    }
    
    public static void main(String[] args) {
        sendPasswordSetMail("muneeb.ahmed0001@gmail.com", "123456");
    }


}
