package com.vinayaka.hostel.service;

import com.vinayaka.hostel.entity.Admin;
import com.vinayaka.hostel.entity.Student;
import com.vinayaka.hostel.repository.AdminRepository;
import com.vinayaka.hostel.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try Admin first
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isPresent()) {
            return new User(admin.get().getUsername(), admin.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }

        // Try Student
        Optional<Student> student = studentRepository.findByCollegePin(username);
        if (student.isPresent()) {
            return new User(student.get().getCollegePin(), student.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")));
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}
