package com.surakshasetu.admin.service;

import java.util.List;

import com.surakshasetu.admin.entity.Student;

public interface StudentService {

    List<Student> getAllStudents();

    Student saveStudent(Student student);

    Student getStudentById(Long id);

    void deleteStudent(Long id);

    List<Student> getStudentsByDepartment(Long departmentId);

    List<Student> getStudentsByCourse(Long courseId);

    List<Student> getStudentsBySemester(Long semesterId);

    boolean existsByEmail(String email);

    boolean existsByEnrollmentNo(String enrollmentNo);

}