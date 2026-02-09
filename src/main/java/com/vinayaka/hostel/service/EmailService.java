package com.vinayaka.hostel.service;

public interface EmailService {
    void sendResetLink(String to, String token);
    void sendEmail(String to, String subject, String body);
}
