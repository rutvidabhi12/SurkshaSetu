
package com.surakshasetu.admin.controller.api;

import com.surakshasetu.admin.dto.StudentLoginRequest;
import com.surakshasetu.admin.dto.StudentLoginResponse;
import com.surakshasetu.admin.entity.Student;
import com.surakshasetu.admin.repository.StudentRepository;

import jakarta.servlet.http.HttpSession;

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
            @RequestBody StudentLoginRequest request,
            HttpSession session) {

        Student student = studentRepository
                .findByEnrollmentNo(request.getEnrollmentNo())
                .orElse(null);

        if (student == null) {
            return ResponseEntity.ok(
                    new StudentLoginResponse(
                            false,
                            "Invalid enrollment number or password",
                            null, null, null
                    )
            );
        }


        if (!passwordEncoder.matches(
                request.getPassword(),
                student.getPassword())) {

            return ResponseEntity.ok(
                    new StudentLoginResponse(
                            false,
                            "Invalid enrollment number or password",
                            null, null, null
                    )
            );
        }

        if (!student.isActive()) {
            return ResponseEntity.ok(
                    new StudentLoginResponse(
                            false,
                            "Student account is inactive",
                            null, null, null
                    )
            );
        }

        String studentName = student.getFirstName();

        if (student.getMiddleName() != null
                && !student.getMiddleName().isBlank()) {
            studentName += " " + student.getMiddleName();
        }

        studentName += " " + student.getLastName();

        // Store authenticated student information in session
        session.setAttribute("STUDENT_ID", student.getId());
        session.setAttribute("STUDENT_NAME", studentName);
        session.setAttribute("STUDENT_ENROLLMENT", student.getEnrollmentNo());

        System.out.println("Student Session Created: " + session.getId()
        + " | Student ID: " + session.getAttribute("STUDENT_ID"));

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

        @PostMapping("/logout")
        public ResponseEntity<StudentLoginResponse> logout(HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok(
                new StudentLoginResponse(
                        true,
                        "Student logout successful",
                        null, null, null
                )
        );

        }
}
