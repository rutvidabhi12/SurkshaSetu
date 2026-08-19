package com.surakshasetu.faculty.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    // =========================
    // GET ALL FACULTY
    // =========================

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {

        return ResponseEntity.ok(
                facultyService.getAllFaculties()
        );
    }


    // =========================
    // GET FACULTY BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(
            @PathVariable Long id) {

        return facultyService
                .getFacultyById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }


    // =========================
    // POST / ADD FACULTY
    // =========================

    @PostMapping
    public ResponseEntity<Faculty> saveFaculty(
            @RequestBody Faculty faculty) {

        Faculty savedFaculty =
                facultyService.saveFaculty(faculty);

        return ResponseEntity.ok(savedFaculty);
    }

    // =========================
// PUT / UPDATE FACULTY
// =========================

@PutMapping("/{id}")
public ResponseEntity<Faculty> updateFaculty(
        @PathVariable Long id,
        @RequestBody Faculty faculty) {

    Faculty updatedFaculty =
            facultyService.updateFaculty(id, faculty);

    if (updatedFaculty == null) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(updatedFaculty);
}

        // =========================
        // DELETE FACULTY
        // =========================

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteFaculty(
                @PathVariable Long id) {

        Optional<Faculty> faculty =
                facultyService.getFacultyById(id);

        if (faculty.isEmpty()) {
                return ResponseEntity.notFound().build();
        }

        facultyService.deleteFaculty(id);

        return ResponseEntity.noContent().build();
        }
}