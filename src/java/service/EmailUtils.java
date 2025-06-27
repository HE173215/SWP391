package service;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtils {

    private static final String EMAIL_USERNAME = "tdmanh051803@gmail.com";
    private static final String EMAIL_PASSWORD = "gxlg xifj lwyf kpmq";

    // Method to send a simple text email
    public static void sendTextEmail(String recipientEmail, String subject, String body) {
        sendEmail(recipientEmail, subject, body, "text/plain");
    }

    // Method to send an HTML email
    public static void sendHTMLEmail(String recipientEmail, String subject, String body) {
        sendEmail(recipientEmail, subject, body, "text/html");
    }

    // Unified method to handle email sending
    private static void sendEmail(String recipientEmail, String subject, String body, String contentType) {
        // Set up mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setContent(body, contentType);

            // Send the message
            Transport.send(message);
            System.out.println("Email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send the email to " + recipientEmail);
            e.printStackTrace();
        }
    }

    // Method to generate a 6-digit OTP
    public static String generateOTP() {
        final String CHARACTERS = "0123456789";
        final int OTP_LENGTH = 6;
        Random random = new Random();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);

        for (int i = 0; i < OTP_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            otp.append(randomChar);
        }
        return otp.toString();
    }

    //Tạo username từ email
    public static String generateUsername(String email) {
        return email.substring(0, email.indexOf("@"));
    }

    //Tạo mật khẩu ngẫu nhiên
    public static String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }
    // Example usage (can be uncommented for testing)
/* 
    public static void main(String[] args) {
        String recipient = "mnguyenan94@gmail.com";
        String subject = "Test Email";
        String message = "This is a test email.";

        sendTextEmail(recipient, subject, message);
        String otp = generateOTP();
        System.out.println("Generated OTP: " + otp);
    }
     */
}
