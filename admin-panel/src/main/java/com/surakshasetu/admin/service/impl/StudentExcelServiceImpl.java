package com.surakshasetu.admin.service.impl;

import java.io.ByteArrayOutputStream;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.surakshasetu.admin.entity.Course;
import com.surakshasetu.admin.entity.Department;
import com.surakshasetu.admin.entity.Semester;
import com.surakshasetu.admin.entity.Student;
import com.surakshasetu.admin.service.CourseService;
import com.surakshasetu.admin.service.DepartmentService;
import com.surakshasetu.admin.service.SemesterService;
import com.surakshasetu.admin.service.StudentExcelService;
import com.surakshasetu.admin.service.StudentService;

@Service
public class StudentExcelServiceImpl implements StudentExcelService {

    private final StudentService studentService;
    private final DepartmentService departmentService;
    private final CourseService courseService;
    private final SemesterService semesterService;

    public StudentExcelServiceImpl(
            StudentService studentService,
            DepartmentService departmentService,
            CourseService courseService,
            SemesterService semesterService) {

        this.studentService = studentService;
        this.departmentService = departmentService;
        this.courseService = courseService;
        this.semesterService = semesterService;
    }

    @Override
    public ByteArrayResource downloadTemplate() {

        try {

            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet("Students");

            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("Enrollment No");
            header.createCell(1).setCellValue("First Name");
            header.createCell(2).setCellValue("Last Name");
            header.createCell(3).setCellValue("Gender");
            header.createCell(4).setCellValue("Mobile");
            header.createCell(5).setCellValue("Email");

            for (int i = 0; i <= 5; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            workbook.write(out);

            workbook.close();

            return new ByteArrayResource(
                    out.toByteArray());

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate Excel Template.",
                    e);
        }
    }

    @Override
    public void importStudents(
            MultipartFile file,
            Long departmentId,
            Long courseId,
            Long semesterId) {

        try {

            // =========================================
            // Get Department
            // =========================================

            Department department =
                    departmentService
                            .getDepartmentById(departmentId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Selected Department not found.")
                            );


            // =========================================
            // Get Course
            // =========================================

            Course course =
                    courseService
                            .getCourseById(courseId);

            if (course == null) {

                throw new RuntimeException(
                        "Selected Course not found.");
            }


            // =========================================
            // Get Semester
            // =========================================

            Semester semester =
                    semesterService
                            .getSemesterById(semesterId);

            if (semester == null) {

                throw new RuntimeException(
                        "Selected Semester not found.");
            }


            // =========================================
            // Open Excel
            // =========================================

            XSSFWorkbook workbook =
                    new XSSFWorkbook(
                            file.getInputStream());

            XSSFSheet sheet =
                    workbook.getSheetAt(0);

            DataFormatter formatter =
                    new DataFormatter();


            // =========================================
            // Store Excel Values
            // =========================================

            Set<String> excelEmails =
                    new HashSet<>();

            Set<String> excelEnrollments =
                    new HashSet<>();


            // =========================================
            // First Pass
            // Validate All Rows
            // =========================================

            for (int i = 1;
                 i <= sheet.getLastRowNum();
                 i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                int excelRowNumber = i + 1;


                // =====================================
                // Read Excel Data
                // =====================================

                String enrollmentNo =
                        formatter.formatCellValue(
                                row.getCell(0)).trim();

                String firstName =
                        formatter.formatCellValue(
                                row.getCell(1)).trim();

                String lastName =
                        formatter.formatCellValue(
                                row.getCell(2)).trim();

                String gender =
                        formatter.formatCellValue(
                                row.getCell(3)).trim();

                String mobile =
                        formatter.formatCellValue(
                                row.getCell(4)).trim();

                String email =
                        formatter.formatCellValue(
                                row.getCell(5)).trim();


                // =====================================
                // Skip Empty Row
                // =====================================

                if (enrollmentNo.isEmpty()
                        && firstName.isEmpty()
                        && lastName.isEmpty()
                        && gender.isEmpty()
                        && mobile.isEmpty()
                        && email.isEmpty()) {

                    continue;
                }


                // =====================================
                // Required Field Validation
                // =====================================

                if (enrollmentNo.isEmpty()) {

                    throw new RuntimeException(
                            "Excel Row "
                            + excelRowNumber
                            + ": Enrollment No is required.");
                }

                if (firstName.isEmpty()) {

                    throw new RuntimeException(
                            "Excel Row "
                            + excelRowNumber
                            + ": First Name is required.");
                }

                if (lastName.isEmpty()) {

                    throw new RuntimeException(
                            "Excel Row "
                            + excelRowNumber
                            + ": Last Name is required.");
                }

                if (gender.isEmpty()) {

                    throw new RuntimeException(
                            "Excel Row "
                            + excelRowNumber
                            + ": Gender is required.");
                }

                if (mobile.isEmpty()) {

                    throw new RuntimeException(
                            "Excel Row "
                            + excelRowNumber
                            + ": Mobile is required.");
                }

                if (email.isEmpty()) {

                    throw new RuntimeException(
                            "Excel Row "
                            + excelRowNumber
                            + ": Email is required.");
                }


                // =====================================
                // Duplicate Email Inside Excel
                // =====================================

                if (!excelEmails.add(
                        email.toLowerCase())) {

                    throw new RuntimeException(
                            "Excel Row "
                            + excelRowNumber
                            + ": Email '"
                            + email
                            + "' is duplicated in Excel.");
                }


                // =====================================
                // Duplicate Enrollment Inside Excel
                // =====================================

                if (!excelEnrollments.add(
                        enrollmentNo.toLowerCase())) {

                    throw new RuntimeException(
                            "Excel Row "
                            + excelRowNumber
                            + ": Enrollment No '"
                            + enrollmentNo
                            + "' is duplicated in Excel.");
                }


                // =====================================
                // Duplicate Email in Database
                // =====================================

                if (studentService
                        .existsByEmail(email)) {

                    throw new RuntimeException(
                            "Excel Row "
                            + excelRowNumber
                            + ": Email '"
                            + email
                            + "' already exists in database.");
                }


                // =====================================
                // Duplicate Enrollment in Database
                // =====================================

                if (studentService
                        .existsByEnrollmentNo(
                                enrollmentNo)) {

                    throw new RuntimeException(
                            "Excel Row "
                            + excelRowNumber
                            + ": Enrollment No '"
                            + enrollmentNo
                            + "' already exists in database.");
                }
            }


            // =========================================
            // Second Pass
            // Save Students
            // =========================================

            int importedCount = 0;

            for (int i = 1;
                 i <= sheet.getLastRowNum();
                 i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }


                String enrollmentNo =
                        formatter.formatCellValue(
                                row.getCell(0)).trim();

                String firstName =
                        formatter.formatCellValue(
                                row.getCell(1)).trim();

                String lastName =
                        formatter.formatCellValue(
                                row.getCell(2)).trim();

                String gender =
                        formatter.formatCellValue(
                                row.getCell(3)).trim();

                String mobile =
                        formatter.formatCellValue(
                                row.getCell(4)).trim();

                String email =
                        formatter.formatCellValue(
                                row.getCell(5)).trim();


                // Skip Empty Row

                if (enrollmentNo.isEmpty()
                        && firstName.isEmpty()
                        && lastName.isEmpty()
                        && gender.isEmpty()
                        && mobile.isEmpty()
                        && email.isEmpty()) {

                    continue;
                }


                // =====================================
                // Automatic Password
                // =====================================

                String password =
                        enrollmentNo
                        + "@"
                        + Year.now().getValue();


                // =====================================
                // Create Student
                // =====================================

                Student student =
                        new Student();

                student.setDepartment(
                        department);

                student.setCourse(
                        course);

                student.setSemester(
                        semester);

                student.setEnrollmentNo(
                        enrollmentNo);

                student.setFirstName(
                        firstName);

                student.setLastName(
                        lastName);

                student.setGender(
                        gender);

                student.setMobile(
                        mobile);

                student.setEmail(
                        email);

                student.setPassword(
                        password);

                student.setActive(true);


                // =====================================
                // Save Student
                // =====================================

                studentService.saveStudent(
                        student);

                importedCount++;
            }


            workbook.close();


            System.out.println(
                    "Students Imported Successfully : "
                    + importedCount);

        } catch (RuntimeException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to read Excel File.",
                    e);
        }
    }
}