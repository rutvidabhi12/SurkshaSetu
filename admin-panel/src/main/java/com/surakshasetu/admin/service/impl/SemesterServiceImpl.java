package com.surakshasetu.admin.service.impl;

import com.surakshasetu.admin.entity.Semester;
import com.surakshasetu.admin.repository.SemesterRepository;
import com.surakshasetu.admin.service.SemesterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;

    public SemesterServiceImpl(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    @Override
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

    @Override
    public List<Semester> getSemestersByCourse(Long courseId) {
        return semesterRepository.findByCourseId(courseId);
    }

    @Override
    public Semester saveSemester(Semester semester) {
        return semesterRepository.save(semester);
    }

    @Override
    public Semester getSemesterById(Long id) {
        return semesterRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteSemester(Long id) {
        semesterRepository.deleteById(id);
    }
}