package com.vinayaka.hostel.controller;

import com.vinayaka.hostel.dto.AuthRequest;
import com.vinayaka.hostel.dto.AuthResponse;
import com.vinayaka.hostel.service.CustomUserDetailsService;
import com.vinayaka.hostel.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Explicitly allow CORS for auth endpoints
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private com.vinayaka.hostel.repository.StudentRepository studentRepository;

    @Autowired
    private com.vinayaka.hostel.repository.AdminRepository adminRepository;


    @org.springframework.beans.factory.annotation.Value("${app.frontend.url}")
    private String frontendUrl;

    @Autowired
    private com.vinayaka.hostel.service.EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();
            
            final String token = jwtUtils.generateToken(userDetails, role, 0L);

            return ResponseEntity.ok(new AuthResponse(token, role, 0L, userDetails.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody java.util.Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        try {
            com.vinayaka.hostel.entity.Student student = null;
            try {
                student = studentRepository.findByEmail(email).orElse(null);
            } catch (org.springframework.dao.IncorrectResultSizeDataAccessException e) {
                // Handle duplicate emails (hackathon fix: pick one or show error)
                return ResponseEntity.status(500).body("Error: Multiple accounts found with this email. Please contact admin.");
            }

            if (student != null) {
                String token = java.util.UUID.randomUUID().toString();
                // In a real app, save token with expiry
                // Send reset link using EmailService interface
                emailService.sendResetLink(email, token);
                // HACK: Return token in response for testing since user can't see server logs
                return ResponseEntity.ok(java.util.Map.of(
                    "message", "Password reset link generated (Check logs or use debug token below).",
                    "debug_token", token,
                    "debug_link", frontendUrl + "/reset-password?token=" + token
                ));
            }

            // Check Admin
            com.vinayaka.hostel.entity.Admin admin = adminRepository.findByEmail(email).orElse(null);
            if (admin != null) {
                String token = java.util.UUID.randomUUID().toString();
                emailService.sendResetLink(email, token);
                return ResponseEntity.ok(java.util.Map.of(
                    "message", "Password reset link generated.",
                    "debug_token", token,
                    "debug_link", frontendUrl + "/reset-password?token=" + token
                ));
            }

            return ResponseEntity.status(404).body("Email not found");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}
