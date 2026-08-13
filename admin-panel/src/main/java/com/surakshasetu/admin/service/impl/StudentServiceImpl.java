package com.surakshasetu.admin.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.surakshasetu.admin.entity.Student;
import com.surakshasetu.admin.repository.StudentRepository;
import com.surakshasetu.admin.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {

        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {

        /*
         * UPDATE STUDENT
         */
        if (student.getId() != null) {

            Student existingStudent = studentRepository
                    .findById(student.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Student not found"));

            /*
             * Password Edit page માંથી નથી આવતો,
             * એટલે જૂનો password જ રાખવો.
             */
            if (student.getPassword() == null
                    || student.getPassword().isBlank()) {

                student.setPassword(
                        existingStudent.getPassword()
                );

            } else {

                /*
                 * New password આપ્યો હોય તો
                 * BCrypt encode કરવો.
                 */
                if (!student.getPassword().startsWith("$2a$")) {

                    student.setPassword(
                            passwordEncoder.encode(
                                    student.getPassword()
                            )
                    );
                }
            }

        }

        /*
         * NEW STUDENT
         */
        else {

            if (student.getPassword() != null
                    && !student.getPassword().isBlank()) {

                String password = student.getPassword();

                if (!password.startsWith("$2a$")) {

                    student.setPassword(
                            passwordEncoder.encode(password)
                    );
                }
            }
        }

        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {

        return studentRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public void deleteStudent(Long id) {

        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getStudentsByDepartment(Long departmentId) {

        return studentRepository
                .findByDepartmentId(departmentId);
    }

    @Override
    public List<Student> getStudentsByCourse(Long courseId) {

        return studentRepository
                .findByCourseId(courseId);
    }

    @Override
    public List<Student> getStudentsBySemester(Long semesterId) {

        return studentRepository
                .findBySemesterId(semesterId);
    }

    @Override
    public boolean existsByEmail(String email) {

        return studentRepository
                .existsByEmail(email);
    }

    @Override
    public boolean existsByEnrollmentNo(String enrollmentNo) {

        return studentRepository
                .existsByEnrollmentNo(enrollmentNo);
    }
}