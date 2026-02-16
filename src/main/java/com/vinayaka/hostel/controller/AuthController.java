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


}
