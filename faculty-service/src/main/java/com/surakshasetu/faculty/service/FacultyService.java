package com.surakshasetu.faculty.service;

import com.surakshasetu.faculty.entity.Faculty;

import java.util.List;
import java.util.Optional;

public interface FacultyService {

    List<Faculty> getAllFaculties();

    Optional<Faculty> getFacultyById(Long id);

    Optional<Faculty> getFacultyByCode(String facultyCode);

    Optional<Faculty> getFacultyByEmail(String email);

    Faculty saveFaculty(Faculty faculty);

    Faculty updateFaculty(Long id, Faculty faculty);

    void deleteFaculty(Long id);

    boolean existsByFacultyCode(String facultyCode);

    boolean existsByEmail(String email);
}