package com.surakshasetu.admin.service;

import java.util.List;

import com.surakshasetu.admin.entity.Course;

public interface CourseService {

    List<Course> getAllCourses();

    List<Course> getCoursesByDepartment(Long departmentId);

    Course saveCourse(Course course);

    Course getCourseById(Long id);

    void deleteCourse(Long id);

    Course updateCourse(Long id, Course course);
}