package com.surakshasetu.admin.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.surakshasetu.admin.entity.Admin;
import com.surakshasetu.admin.repository.AdminRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeAdmin(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Check whether admin already exists
            if (!adminRepository.existsByUsername("admin")) {

                Admin admin = Admin.builder()
                        .fullName("System Administrator")
                        .username("admin")
                        .email("admin@surakshasetu.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role("ADMIN")
                        .status("ACTIVE")
                        .createdAt(LocalDateTime.now())
                        .build();

                adminRepository.save(admin);

                System.out.println(
                        "===================================="
                );
                System.out.println(
                        "DEFAULT ADMIN CREATED"
                );
                System.out.println(
                        "Username : admin"
                );
                System.out.println(
                        "Password : admin123"
                );
                System.out.println(
                        "===================================="
                );

            } else {

                System.out.println(
                        "Admin already exists. No changes made."
                );
            }
        };
    }
}