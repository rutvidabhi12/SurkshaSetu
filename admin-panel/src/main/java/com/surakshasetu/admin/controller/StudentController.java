package com.surakshasetu.admin.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.surakshasetu.admin.service.SemesterService;

import com.surakshasetu.admin.entity.Student;
import com.surakshasetu.admin.service.CourseService;
import com.surakshasetu.admin.service.DepartmentService;
import com.surakshasetu.admin.service.StudentExcelService;
import com.surakshasetu.admin.service.StudentService;

@Controller
public class StudentController {

    private final StudentExcelService studentExcelService;
    private final StudentService studentService;
    private final DepartmentService departmentService;
    private final CourseService courseService;
    private final SemesterService semesterService;

    public StudentController(
            StudentExcelService studentExcelService,
            StudentService studentService,
            DepartmentService departmentService,
            CourseService courseService,
            SemesterService semesterService) {

        this.studentExcelService = studentExcelService;
        this.studentService = studentService;
        this.departmentService = departmentService;
        this.courseService = courseService;
        this.semesterService = semesterService;
    }

    // =========================================
    // Student Page
    // =========================================

    @GetMapping("/students")
    public String studentPage(Model model) {

        model.addAttribute(
                "students",
                studentService.getAllStudents()
        );

        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );

        model.addAttribute(
                "courses",
                courseService.getAllCourses()
        );

        model.addAttribute(
                "semesters",
                semesterService.getAllSemesters()
        );

        return "student/index";
    }

    // =========================================
    // Download Excel Template
    // =========================================

    @GetMapping("/students/template")
    public ResponseEntity<ByteArrayResource> downloadTemplate() {

        ByteArrayResource resource =
                studentExcelService.downloadTemplate();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=student_template.xlsx"
                )
                .contentType(
                        MediaType.APPLICATION_OCTET_STREAM
                )
                .body(resource);
    }

    // =========================================
    // Upload Excel
    // =========================================

    @PostMapping("/students/upload")
    public String uploadExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("departmentId") Long departmentId,
            @RequestParam("courseId") Long courseId) {

        if (file.isEmpty()) {

            return "redirect:/students?error=Please select an Excel file";
        }

        try {

            studentExcelService.importStudents(
                    file,
                    departmentId,
                    courseId
            );

            return "redirect:/students?success";

        } catch (RuntimeException e) {

            return "redirect:/students?error="
                    + java.net.URLEncoder.encode(
                            e.getMessage(),
                            java.nio.charset.StandardCharsets.UTF_8
                    );
        }
    }

    // =========================================
    // View Student
    // =========================================

    @GetMapping("/students/view/{id}")
    public String viewStudent(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "student",
                studentService.getStudentById(id)
        );

        return "student/view";
    }

    // =========================================
    // Edit Student
    // =========================================

    @GetMapping("/students/edit/{id}")
    public String editStudent(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "student",
                studentService.getStudentById(id)
        );

        return "student/edit";
    }

    // =========================================
    // Update Student
    // =========================================

    @PostMapping("/students/update")
    public String updateStudent(
            @ModelAttribute Student student) {

        studentService.saveStudent(student);

        return "redirect:/students";
    }

    // =========================================
    // Delete Student
    // =========================================

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(
            @PathVariable Long id) {

        studentService.deleteStudent(id);

        return "redirect:/students";
    }
}