package com.surakshasetu.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surakshasetu.admin.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEnrollmentNo(String enrollmentNo);

    Optional<Student> findByEmail(String email);

    List<Student> findByDepartmentId(Long departmentId);

    List<Student> findByCourseId(Long courseId);

    List<Student> findBySemesterId(Long semesterId);

    boolean existsByEmail(String email);

    boolean existsByEnrollmentNo(String enrollmentNo);

}