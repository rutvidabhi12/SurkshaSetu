package com.surakshasetu.admin.service;

import java.util.List;
import java.util.Optional;

import com.surakshasetu.admin.entity.Semester;

public interface SemesterService {

    List<Semester> getAllSemesters();

    List<Semester> getSemestersByCourse(Long courseId);

    Semester saveSemester(Semester semester);

    Semester getSemesterById(Long id);

    void deleteSemester(Long id);

    Optional<Semester> getSemesterByCourseIdAndSemesterNumber(
            Long courseId,
            Integer semesterNumber
    );
}