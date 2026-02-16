package com.vinayaka.hostel.controller;

import com.vinayaka.hostel.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Adjust as needed for production
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @org.springframework.beans.factory.annotation.Value("${app.frontend.url}")
    private String frontendUrl;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            String token = passwordResetService.processForgotPassword(email);
            
            // HACK: Return token in response for testing since user can't see server logs
            return ResponseEntity.ok(java.util.Map.of(
                "message", "Password reset link generated (Check logs or use debug token below).",
                "debug_token", token,
                "debug_link", frontendUrl + "/reset-password?token=" + token
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Or 400 based on error
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody Map<String, String> request) {
        try {
            String newPassword = request.get("newPassword");
            if (newPassword == null || newPassword.isEmpty()) {
                return ResponseEntity.badRequest().body("New password is required");
            }
            passwordResetService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password reset successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred");
        }
    }
}
