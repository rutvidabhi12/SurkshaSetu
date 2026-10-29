
package com.surakshasetu.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                
                .authorizeHttpRequests(auth -> auth

                        // Admin Login
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // Student Portal: UI Testing Only
                        .requestMatchers(
                                "/student-portal/**"
                        ).permitAll()

                        // Student Login API
                        .requestMatchers("/api/student/login").permitAll()
                        .requestMatchers("/student-portal/**").permitAll()

                        // Faculty Portal: UI Testing Only
                        .requestMatchers("/faculty-portal/**").permitAll()

                        // All other pages require authentication
                        .anyRequest().authenticated()
                )


                // Existing Admin Login
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )

                .logout(Customizer.withDefaults());

        return http.build();
    }
}
