package com.surakshasetu.admin.controller.api;

import com.surakshasetu.admin.dto.StudentLoginRequest;
import com.surakshasetu.admin.dto.StudentLoginResponse;
import com.surakshasetu.admin.entity.Student;
import com.surakshasetu.admin.repository.StudentRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentApiController {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentApiController(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {

        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<StudentLoginResponse> login(
            @RequestBody StudentLoginRequest request) {

        // 1. Find student by enrollment number
        Student student = studentRepository
                .findByEnrollmentNo(request.getEnrollmentNo())
                .orElse(null);

        // 2. Student not found
        if (student == null) {

            return ResponseEntity.ok(
                    new StudentLoginResponse(
                            false,
                            "Invalid enrollment number or password",
                            null,
                            null,
                            null
                    )
            );
        }

        // 3. Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                student.getPassword())) {

            return ResponseEntity.ok(
                    new StudentLoginResponse(
                            false,
                            "Invalid enrollment number or password",
                            null,
                            null,
                            null
                    )
            );
        }

        // 4. Check student active status
        if (!student.isActive()) {

            return ResponseEntity.ok(
                    new StudentLoginResponse(
                            false,
                            "Student account is inactive",
                            null,
                            null,
                            null
                    )
            );
        }

        // 5. Create full student name
        String studentName = student.getFirstName();

        if (student.getMiddleName() != null
                && !student.getMiddleName().isBlank()) {

            studentName += " " + student.getMiddleName();
        }

        studentName += " " + student.getLastName();

        // 6. Login successful
        return ResponseEntity.ok(
                new StudentLoginResponse(
                        true,
                        "Login successful",
                        student.getId(),
                        student.getEnrollmentNo(),
                        studentName
                )
        );
    }
}