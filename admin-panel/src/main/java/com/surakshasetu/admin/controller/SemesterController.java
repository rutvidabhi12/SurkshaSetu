package com.surakshasetu.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.surakshasetu.admin.entity.Semester;
import com.surakshasetu.admin.service.CourseService;
import com.surakshasetu.admin.service.SemesterService;

@Controller
@RequestMapping("/semesters")
public class SemesterController {

    private final SemesterService semesterService;
    private final CourseService courseService;

    public SemesterController(SemesterService semesterService,
                              CourseService courseService) {

        this.semesterService = semesterService;
        this.courseService = courseService;
    }


    // =========================
    // Semester List
    // =========================

    @GetMapping
    public String listSemesters(Model model) {

        model.addAttribute(
                "semesters",
                semesterService.getAllSemesters()
        );

        model.addAttribute(
                "courses",
                courseService.getAllCourses()
        );

        return "semester/index";
    }


    // =========================
    // Add Semester Form
    // =========================

    @GetMapping("/add")
    public String addSemesterForm(Model model) {

        model.addAttribute(
                "semester",
                new Semester()
        );

        model.addAttribute(
                "courses",
                courseService.getAllCourses()
        );

        return "semester/add";
    }


    // =========================
    // Save Semester
    // =========================

    @PostMapping("/save")
    public String saveSemester(
            @ModelAttribute Semester semester) {

        semesterService.saveSemester(semester);

        return "redirect:/semesters?success";
    }


    // =========================
    // Edit Semester Form
    // =========================

    @GetMapping("/edit/{id}")
    public String editSemester(
            @PathVariable Long id,
            Model model) {

        Semester semester =
                semesterService.getSemesterById(id);

        model.addAttribute(
                "semester",
                semester
        );

        model.addAttribute(
                "courses",
                courseService.getAllCourses()
        );

        return "semester/edit";
    }


    // =========================
    // Update Semester
    // =========================

    @PostMapping("/update")
    public String updateSemester(
            @ModelAttribute Semester semester) {

        semesterService.saveSemester(semester);

        return "redirect:/semesters?updated";
    }


    // =========================
    // Delete Semester
    // =========================

    @GetMapping("/delete/{id}")
    public String deleteSemester(
            @PathVariable Long id) {

        semesterService.deleteSemester(id);

        return "redirect:/semesters?deleted";
    }

}