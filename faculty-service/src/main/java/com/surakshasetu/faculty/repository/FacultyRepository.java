package com.surakshasetu.faculty.repository;

import com.surakshasetu.faculty.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Optional<Faculty> findByFacultyCode(String facultyCode);

    Optional<Faculty> findByEmail(String email);

    boolean existsByFacultyCode(String facultyCode);

    boolean existsByEmail(String email);
}