package com.surakshasetu.admin.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.surakshasetu.admin.entity.Admin;
import com.surakshasetu.admin.repository.AdminRepository;
import com.surakshasetu.admin.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {

        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin saveAdmin(Admin admin) {

        admin.setPassword(
                passwordEncoder.encode(
                        admin.getPassword()
                )
        );

        return adminRepository.save(admin);
    }
}