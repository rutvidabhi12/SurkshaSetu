package com.surakshasetu.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.surakshasetu.admin.entity.Course;
import com.surakshasetu.admin.service.CourseService;
import com.surakshasetu.admin.service.DepartmentService;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final DepartmentService departmentService;

    public CourseController(CourseService courseService,
                            DepartmentService departmentService) {

        this.courseService = courseService;
        this.departmentService = departmentService;
    }


    // =========================
    // Course List
    // =========================

    @GetMapping
    public String listCourses(Model model) {

        model.addAttribute(
                "courses",
                courseService.getAllCourses()
        );

        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );

        return "course/index";
    }


    // =========================
    // Add Course Form
    // =========================

    @GetMapping("/add")
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


    // =========================
    // Save Course
    // =========================

    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course) {

        courseService.saveCourse(course);

        return "redirect:/courses?success";
    }


    // =========================
    // Edit Course Form
    // =========================

    @GetMapping("/edit/{id}")
    public String editCourse(@PathVariable Long id,
                             Model model) {

        Course course = courseService.getCourseById(id);

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


    // =========================
    // Update Course
    // =========================

    @PostMapping("/update")
    public String updateCourse(@ModelAttribute Course course) {

        courseService.updateCourse(
                course.getId(),
                course
        );

        return "redirect:/courses?updated";
    }


    // =========================
    // Delete Course
    // =========================

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {

        courseService.deleteCourse(id);

        return "redirect:/courses?deleted";
    }

}