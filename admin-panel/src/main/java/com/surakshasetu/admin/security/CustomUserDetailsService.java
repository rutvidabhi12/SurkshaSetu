package com.surakshasetu.admin.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.surakshasetu.admin.entity.Admin;
import com.surakshasetu.admin.repository.AdminRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public CustomUserDetailsService(
            AdminRepository adminRepository) {

        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Admin admin = adminRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Admin not found"
                        )
                );

        return new User(
                admin.getUsername(),
                admin.getPassword(),
                List.of(
                    new SimpleGrantedAuthority(
                        "ROLE_" + admin.getRole()
                    )
                )
        );
    }
}