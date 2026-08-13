package com.surakshasetu.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.surakshasetu.admin.entity.Course;
import com.surakshasetu.admin.service.CourseService;
import com.surakshasetu.admin.service.DepartmentService;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final DepartmentService departmentService;

    public CourseController(
            CourseService courseService,
            DepartmentService departmentService) {

        this.courseService = courseService;
        this.departmentService = departmentService;
    }


    // =====================================================
    // COURSE LIST + DEPARTMENT FILTER
    // =====================================================

    @GetMapping("/courses")
    public String courseList(
            @RequestParam(required = false) Long departmentId,
            Model model) {

        List<Course> courses;

        if (departmentId != null) {

            courses = courseService
                    .getCoursesByDepartment(departmentId);

        } else {

            courses = courseService
                    .getAllCourses();
        }


        model.addAttribute(
                "courses",
                courses
        );


        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );


        model.addAttribute(
                "selectedDepartment",
                departmentId
        );


        return "course/index";
    }


    // =====================================================
    // ADD COURSE
    // =====================================================

    @GetMapping("/courses/add")
    public String addCourseForm(Model model) {

        model.addAttribute(
                "course",
                new Course()
        );

        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );

        return "course/add";
    }


    // =====================================================
    // SAVE COURSE
    // =====================================================

    @PostMapping("/courses/save")
    public String saveCourse(
            @ModelAttribute Course course) {

        courseService.saveCourse(course);

        return "redirect:/courses";
    }


    // =====================================================
    // EDIT COURSE
    // =====================================================

    @GetMapping("/courses/edit/{id}")
    public String editCourse(
            @PathVariable Long id,
            Model model) {

        Course course =
                courseService.getCourseById(id);

        if (course == null) {

            return "redirect:/courses";
        }


        model.addAttribute(
                "course",
                course
        );


        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );


        return "course/edit";
    }


    // =====================================================
    // UPDATE COURSE
    // =====================================================

    @PostMapping("/courses/update")
    public String updateCourse(
            @ModelAttribute Course course) {

        courseService.updateCourse(
                course.getId(),
                course
        );

        return "redirect:/courses";
    }


    // =====================================================
    // DELETE COURSE
    // =====================================================

    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(
            @PathVariable Long id) {

        courseService.deleteCourse(id);

        return "redirect:/courses";
    }

}