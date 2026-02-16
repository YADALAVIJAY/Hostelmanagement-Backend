package com.vinayaka.hostel.service;

import com.vinayaka.hostel.entity.Admin;
import com.vinayaka.hostel.entity.PasswordResetToken;
import com.vinayaka.hostel.entity.Student;
import com.vinayaka.hostel.repository.AdminRepository;
import com.vinayaka.hostel.repository.PasswordResetTokenRepository;
import com.vinayaka.hostel.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Value("${app.frontend.url}")
    private String frontendUrl;

    @Transactional
    public String processForgotPassword(String email) {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);

        if (studentOpt.isEmpty() && adminOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }

        String token = UUID.randomUUID().toString();
        
        if (studentOpt.isPresent()) {
            createTokenForStudent(studentOpt.get(), token);
        } else {
            createTokenForAdmin(adminOpt.get(), token);
        }

        String resetLink = frontendUrl + "/reset-password?token=" + token;
        String message = "Click the link to reset your password: " + resetLink;
        
        emailService.sendEmail(email, "Password Reset Request", message);
        return token;
    }

    private void createTokenForStudent(Student student, String token) {
        PasswordResetToken resetToken = new PasswordResetToken(token, student);
        tokenRepository.save(resetToken);
    }

    private void createTokenForAdmin(Admin admin, String token) {
        PasswordResetToken resetToken = new PasswordResetToken(token, admin);
        tokenRepository.save(resetToken);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Token expired");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);

        if (resetToken.getStudent() != null) {
            Student student = resetToken.getStudent();
            student.setPassword(encodedPassword);
            studentRepository.save(student);
        } else if (resetToken.getAdmin() != null) {
            Admin admin = resetToken.getAdmin();
            admin.setPassword(encodedPassword);
            adminRepository.save(admin);
        }

        tokenRepository.delete(resetToken);
    }
}
