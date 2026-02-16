package com.vinayaka.hostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
                System.out.println("Email sent successfully to " + to);
            } catch (Exception e) {
                System.err.println("Failed to send email: " + e.getMessage());
                fallbackLog(to, subject, body);
            }
        } else {
            System.out.println("JavaMailSender is not configured.");
            fallbackLog(to, subject, body);
        }
    }

    public void sendResetLink(String to, String token) {
        String subject = "Password Reset Request";
        String link = "https://hostelmanagement-frontend1.onrender.com/reset-password?token=" + token; // Fallback link
        String body = "Click the link to reset your password: " + link;
        sendEmail(to, subject, body);
    }

    private void fallbackLog(String to, String subject, String body) {
        System.out.println("--- EMAIL FALLBACK LOG ---");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: \n" + body);
        System.out.println("--------------------------");
    }
}
