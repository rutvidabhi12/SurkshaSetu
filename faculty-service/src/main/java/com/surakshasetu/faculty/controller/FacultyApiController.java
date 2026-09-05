package com.surakshasetu.faculty.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.surakshasetu.faculty.entity.Faculty;
import com.surakshasetu.faculty.service.FacultyService;

@RestController
@RequestMapping("/api/faculty")
public class FacultyApiController {

    private final FacultyService facultyService;


    public FacultyApiController(
            FacultyService facultyService) {

        this.facultyService = facultyService;
    }


    // =====================================================
    // GET ALL FACULTY
    // =====================================================

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {

        return ResponseEntity.ok(
                facultyService.getAllFaculties()
        );
    }


    // =====================================================
    // GET FACULTY BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(
            @PathVariable Long id) {

        return facultyService
                .getFacultyById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }


    // =====================================================
    // ADD FACULTY
    // =====================================================

    @PostMapping
    public ResponseEntity<Faculty> saveFaculty(
            @RequestBody Faculty faculty) {

        Faculty savedFaculty =
                facultyService.saveFaculty(
                        faculty
                );

        return ResponseEntity.ok(
                savedFaculty
        );
    }


    // =====================================================
    // UPDATE FACULTY
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(
            @PathVariable Long id,
            @RequestBody Faculty faculty) {

        Faculty updatedFaculty =
                facultyService.updateFaculty(
                        id,
                        faculty
                );


        if (updatedFaculty == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        return ResponseEntity.ok(
                updatedFaculty
        );
    }


    // =====================================================
    // UPLOAD FACULTY PHOTO
    // =====================================================

    @PostMapping(
            value = "/{id}/photo",
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Faculty> uploadPhoto(

            @PathVariable Long id,

            @RequestParam("photo")
            MultipartFile photo) {


        // -------------------------------------------------
        // CHECK FACULTY
        // -------------------------------------------------

        Optional<Faculty> optionalFaculty =
                facultyService.getFacultyById(id);


        if (optionalFaculty.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        // -------------------------------------------------
        // CHECK PHOTO
        // -------------------------------------------------

        if (photo == null ||
            photo.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .build();
        }


        try {

            // ---------------------------------------------
            // UPLOAD DIRECTORY
            // ---------------------------------------------

            Path uploadDirectory =
                    Paths.get(
                            "uploads",
                            "faculty"
                    );


            Files.createDirectories(
                    uploadDirectory
            );


            // ---------------------------------------------
            // FILE EXTENSION
            // ---------------------------------------------

            String originalName =
                    photo.getOriginalFilename();


            String extension = "";


            if (originalName != null &&
                originalName.contains(".")) {

                extension =
                        originalName.substring(
                                originalName
                                        .lastIndexOf(".")
                        );
            }


            // ---------------------------------------------
            // UNIQUE FILE NAME
            // ---------------------------------------------

            String fileName =
                    "faculty_"
                    + id
                    + "_"
                    + UUID.randomUUID()
                    + extension;


            // ---------------------------------------------
            // FILE PATH
            // ---------------------------------------------

            Path filePath =
                    uploadDirectory.resolve(
                            fileName
                    );


            // ---------------------------------------------
            // SAVE FILE
            // ---------------------------------------------

            Files.write(
                    filePath,
                    photo.getBytes()
            );


            // ---------------------------------------------
            // UPDATE DATABASE
            // ---------------------------------------------

            Faculty faculty =
                    optionalFaculty.get();


            faculty.setPhoto(
                    fileName
            );


            Faculty updatedFaculty =
                    facultyService.saveFaculty(
                            faculty
                    );


            return ResponseEntity.ok(
                    updatedFaculty
            );


        } catch (IOException e) {

            e.printStackTrace();


            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }


    // =====================================================
    // GET FACULTY PHOTO
    // =====================================================

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> getPhoto(
            @PathVariable Long id) {


        Optional<Faculty> optionalFaculty =
                facultyService.getFacultyById(id);


        if (optionalFaculty.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        Faculty faculty =
                optionalFaculty.get();


        // -------------------------------------------------
        // PHOTO NAME CHECK
        // -------------------------------------------------

        if (faculty.getPhoto() == null ||
            faculty.getPhoto().isBlank()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        try {

            Path filePath =
                    Paths.get(
                            "uploads",
                            "faculty",
                            faculty.getPhoto()
                    );


            // -------------------------------------------------
            // FILE EXISTS CHECK
            // -------------------------------------------------

            if (!Files.exists(filePath)) {

                return ResponseEntity
                        .notFound()
                        .build();
            }


            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );


            // -------------------------------------------------
            // CONTENT TYPE
            // -------------------------------------------------

            String contentType =
                    Files.probeContentType(
                            filePath
                    );


            if (contentType == null) {

                contentType =
                        "application/octet-stream";
            }


            // -------------------------------------------------
            // RETURN PHOTO
            // -------------------------------------------------

            return ResponseEntity
                    .ok()
                    .contentType(
                            MediaType.parseMediaType(
                                    contentType
                            )
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\""
                                    + faculty.getPhoto()
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
    // DELETE FACULTY
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(
            @PathVariable Long id) {


        Optional<Faculty> optionalFaculty =
                facultyService.getFacultyById(id);


        if (optionalFaculty.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        // -------------------------------------------------
        // DELETE DATABASE RECORD
        // -------------------------------------------------

        facultyService.deleteFaculty(id);


        return ResponseEntity
                .noContent()
                .build();
    }

}