package com.surakshasetu.faculty.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.surakshasetu.faculty.entity.Faculty;
import com.surakshasetu.faculty.repository.FacultyRepository;
import com.surakshasetu.faculty.service.FacultyService;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(
            FacultyRepository facultyRepository) {

        this.facultyRepository = facultyRepository;
    }


    // Get all faculty
    @Override
    public List<Faculty> getAllFaculties() {

        return facultyRepository.findAll();
    }


    // Get faculty by ID
    @Override
    public Optional<Faculty> getFacultyById(Long id) {

        return facultyRepository.findById(id);
    }


    // Get faculty by faculty code
    @Override
    public Optional<Faculty> getFacultyByCode(
            String facultyCode) {

        return facultyRepository
                .findByFacultyCode(facultyCode);
    }


    // Get faculty by email
    @Override
    public Optional<Faculty> getFacultyByEmail(
            String email) {

        return facultyRepository
                .findByEmail(email);
    }


    // Save faculty
    @Override
    public Faculty saveFaculty(Faculty faculty) {

        return facultyRepository.save(faculty);
    }


    // Update faculty
    @Override
    public Faculty updateFaculty(
            Long id,
            Faculty faculty) {

        Optional<Faculty> existingFaculty =
                facultyRepository.findById(id);

        if (existingFaculty.isEmpty()) {
            return null;
        }

        Faculty existing =
                existingFaculty.get();

        existing.setFacultyCode(
                faculty.getFacultyCode()
        );

        existing.setFirstName(
                faculty.getFirstName()
        );

        existing.setMiddleName(
                faculty.getMiddleName()
        );

        existing.setLastName(
                faculty.getLastName()
        );

        existing.setEmail(
                faculty.getEmail()
        );

        existing.setMobile(
                faculty.getMobile()
        );

        existing.setDepartment(
                faculty.getDepartment()
        );

        existing.setDesignation(
                faculty.getDesignation()
        );

        existing.setGender(
                faculty.getGender()
        );

        existing.setPhoto(
                faculty.getPhoto()
        );

        existing.setActive(
                faculty.isActive()
        );

        return facultyRepository.save(existing);
    }


    // Delete faculty
    @Override
    public void deleteFaculty(Long id) {

        facultyRepository.deleteById(id);
    }


    // Check faculty code
    @Override
    public boolean existsByFacultyCode(
            String facultyCode) {

        return facultyRepository
                .existsByFacultyCode(facultyCode);
    }


    // Check email
    @Override
    public boolean existsByEmail(
            String email) {

        return facultyRepository
                .existsByEmail(email);
    }
}