package com.vinayaka.hostel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MockEmailService implements EmailService {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void sendResetLink(String to, String token) {
        System.out.println("=================================================");
        System.out.println("MOCK EMAIL SERVICE");
        System.out.println("To: " + to);
        System.out.println("Subject: Password Reset Request");
        System.out.println(
            "Body: Click the link to reset your password: "
            + frontendUrl
            + "/reset-password?token="
            + token
        );
        System.out.println("=================================================");
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        System.out.println("=================================================");
        System.out.println("MOCK EMAIL SERVICE (Generic)");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("=================================================");
    }
}
