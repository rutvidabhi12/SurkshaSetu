package com.surakshasetu.admin.controller.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.surakshasetu.admin.dto.StudentLoginRequest;
import com.surakshasetu.admin.dto.StudentLoginResponse;
import com.surakshasetu.admin.entity.Student;
import com.surakshasetu.admin.repository.StudentRepository;

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


    // =====================================================
    // STUDENT LOGIN
    // =====================================================

    @PostMapping("/login")
    public ResponseEntity<StudentLoginResponse> login(
            @RequestBody StudentLoginRequest request) {

        Student student =
                studentRepository
                        .findByEnrollmentNo(
                                request.getEnrollmentNo()
                        )
                        .orElse(null);


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


        String studentName =
                student.getFirstName();


        if (student.getMiddleName() != null
                && !student.getMiddleName().isBlank()) {

            studentName +=
                    " " + student.getMiddleName();
        }


        studentName +=
                " " + student.getLastName();


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


    // =====================================================
    // UPLOAD / CHANGE STUDENT PHOTO
    // =====================================================

    @PostMapping(
            value = "/{id}/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Student> uploadPhoto(

            @PathVariable Long id,

            @RequestParam("photo")
            MultipartFile photo) {


        Optional<Student> optionalStudent =
                studentRepository.findById(id);


        if (optionalStudent.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        if (photo == null ||
                photo.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .build();
        }


        try {

            // =========================================
            // UPLOAD DIRECTORY
            // =========================================

            Path uploadDirectory =
                    Paths.get(
                            "uploads",
                            "students"
                    );


            Files.createDirectories(
                    uploadDirectory
            );


            // =========================================
            // FILE EXTENSION
            // =========================================

            String originalName =
                    photo.getOriginalFilename();


            String extension = "";


            if (originalName != null
                    && originalName.contains(".")) {

                extension =
                        originalName.substring(
                                originalName.lastIndexOf(".")
                        );
            }


            // =========================================
            // UNIQUE FILE NAME
            // =========================================

            String fileName =
                    "student_"
                    + id
                    + "_"
                    + UUID.randomUUID()
                    + extension;


            // =========================================
            // SAVE FILE
            // =========================================

            Path filePath =
                    uploadDirectory.resolve(
                            fileName
                    );


            Files.write(
                    filePath,
                    photo.getBytes()
            );


            // =========================================
            // UPDATE DATABASE
            // =========================================

            Student student =
                    optionalStudent.get();


            student.setPhoto(
                    fileName
            );


            Student updatedStudent =
                    studentRepository.save(
                            student
                    );


            return ResponseEntity.ok(
                    updatedStudent
            );


        } catch (IOException e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }


    // =====================================================
    // GET STUDENT PHOTO
    // =====================================================

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> getPhoto(
            @PathVariable Long id) {


        Optional<Student> optionalStudent =
                studentRepository.findById(id);


        if (optionalStudent.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        Student student =
                optionalStudent.get();


        if (student.getPhoto() == null
                || student.getPhoto().isBlank()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        try {

            Path filePath =
                    Paths.get(
                            "uploads",
                            "students",
                            student.getPhoto()
                    );


            if (!Files.exists(filePath)) {

                return ResponseEntity
                        .notFound()
                        .build();
            }


            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );


            String contentType =
                    Files.probeContentType(
                            filePath
                    );


            if (contentType == null) {

                contentType =
                        "application/octet-stream";
            }


            return ResponseEntity.ok()
                    .contentType(
                            MediaType.parseMediaType(
                                    contentType
                            )
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\""
                                    + student.getPhoto()
                                    + "\""
                    )
                    .body(resource);


        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }


    // =====================================================
    // DELETE STUDENT
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id) {


        Optional<Student> student =
                studentRepository.findById(id);


        if (student.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        studentRepository.deleteById(id);


        return ResponseEntity
                .noContent()
                .build();
    }
}