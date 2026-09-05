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


    // =====================================================
    // GET ALL FACULTY
    // =====================================================

    @Override
    public List<Faculty> getAllFaculties() {

        return facultyRepository.findAll();
    }


    // =====================================================
    // GET FACULTY BY ID
    // =====================================================

    @Override
    public Optional<Faculty> getFacultyById(
            Long id) {

        return facultyRepository.findById(id);
    }


    // =====================================================
    // GET FACULTY BY CODE
    // =====================================================

    @Override
    public Optional<Faculty> getFacultyByCode(
            String facultyCode) {

        return facultyRepository
                .findByFacultyCode(facultyCode);
    }


    // =====================================================
    // GET FACULTY BY EMAIL
    // =====================================================

    @Override
    public Optional<Faculty> getFacultyByEmail(
            String email) {

        return facultyRepository
                .findByEmail(email);
    }


    // =====================================================
    // SAVE FACULTY
    // =====================================================

    @Override
    public Faculty saveFaculty(
            Faculty faculty) {

        return facultyRepository.save(faculty);
    }


    // =====================================================
    // UPDATE FACULTY
    // =====================================================

    @Override
    public Faculty updateFaculty(
            Long id,
            Faculty faculty) {

        Optional<Faculty> optionalFaculty =
                facultyRepository.findById(id);


        if (optionalFaculty.isEmpty()) {

            return null;
        }


        Faculty existing =
                optionalFaculty.get();


        // =================================================
        // FACULTY CODE
        // =================================================

        if (faculty.getFacultyCode() != null &&
            !faculty.getFacultyCode().isBlank()) {

            existing.setFacultyCode(
                    faculty.getFacultyCode()
            );
        }


        // =================================================
        // FIRST NAME
        // =================================================

        if (faculty.getFirstName() != null &&
            !faculty.getFirstName().isBlank()) {

            existing.setFirstName(
                    faculty.getFirstName()
            );
        }


        // =================================================
        // MIDDLE NAME
        // =================================================

        existing.setMiddleName(
                faculty.getMiddleName()
        );


        // =================================================
        // LAST NAME
        // =================================================

        if (faculty.getLastName() != null &&
            !faculty.getLastName().isBlank()) {

            existing.setLastName(
                    faculty.getLastName()
            );
        }


        // =================================================
        // EMAIL
        // =================================================

        if (faculty.getEmail() != null &&
            !faculty.getEmail().isBlank()) {

            existing.setEmail(
                    faculty.getEmail()
            );
        }


        // =================================================
        // MOBILE
        // =================================================

        if (faculty.getMobile() != null &&
            !faculty.getMobile().isBlank()) {

            existing.setMobile(
                    faculty.getMobile()
            );
        }


        // =================================================
        // DEPARTMENT
        // =================================================

        if (faculty.getDepartment() != null &&
            !faculty.getDepartment().isBlank()) {

            existing.setDepartment(
                    faculty.getDepartment()
            );
        }


        // =================================================
        // DESIGNATION
        // =================================================

        if (faculty.getDesignation() != null &&
            !faculty.getDesignation().isBlank()) {

            existing.setDesignation(
                    faculty.getDesignation()
            );
        }


        // =================================================
        // GENDER
        // =================================================

        if (faculty.getGender() != null &&
            !faculty.getGender().isBlank()) {

            existing.setGender(
                    faculty.getGender()
            );
        }


        // =================================================
        // PHOTO
        //
        // Photo null હોય તો જૂનો photo રાખશે.
        // =================================================

        if (faculty.getPhoto() != null &&
            !faculty.getPhoto().isBlank()) {

            existing.setPhoto(
                    faculty.getPhoto()
            );
        }


        // =================================================
        // ACTIVE
        // =================================================

        existing.setActive(
                faculty.isActive()
        );


        // =================================================
        // SAVE UPDATED FACULTY
        // =================================================

        return facultyRepository.save(
                existing
        );
    }


    // =====================================================
    // DELETE FACULTY
    // =====================================================

    @Override
    public void deleteFaculty(
            Long id) {

        facultyRepository.deleteById(id);
    }


    // =====================================================
    // CHECK FACULTY CODE
    // =====================================================

    @Override
    public boolean existsByFacultyCode(
            String facultyCode) {

        return facultyRepository
                .existsByFacultyCode(facultyCode);
    }


    // =====================================================
    // CHECK EMAIL
    // =====================================================

    @Override
    public boolean existsByEmail(
            String email) {

        return facultyRepository
                .existsByEmail(email);
    }

}