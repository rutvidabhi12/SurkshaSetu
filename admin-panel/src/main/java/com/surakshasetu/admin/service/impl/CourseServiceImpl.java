package com.surakshasetu.admin.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.surakshasetu.admin.entity.Course;
import com.surakshasetu.admin.repository.CourseRepository;
import com.surakshasetu.admin.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> getCoursesByDepartment(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId);
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public Course updateCourse(Long id, Course course) {

        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        existingCourse.setCourseName(course.getCourseName());
        existingCourse.setTotalSemester(course.getTotalSemester());
        existingCourse.setStatus(course.getStatus());
        existingCourse.setDepartment(course.getDepartment());

        return courseRepository.save(existingCourse);
    }
}