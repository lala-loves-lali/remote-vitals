package com.remote_vitals.mailSender;


import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailTemplate {

    private static final String senderEmail = "muneeb.ahmed0001@gmail.com"; // your email
    private static final String senderPassword = "ilvi nkjk apdk hqmt"; // app password

    public static void sendEmail(String recipientEmail, String subject, String messageText) {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Get the Session object
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );
            message.setSubject(subject);
            message.setText(messageText);

            // Send message
            Transport.send(message);
            System.out.println("Email sent to " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
