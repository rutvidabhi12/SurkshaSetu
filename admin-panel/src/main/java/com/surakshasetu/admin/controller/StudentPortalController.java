package com.surakshasetu.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;
import com.surakshasetu.admin.entity.Student;
import com.surakshasetu.admin.repository.StudentRepository;
import org.springframework.ui.Model;
import com.surakshasetu.admin.service.StudentComplaintService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.surakshasetu.admin.repository.DepartmentRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;
import java.util.HashMap;
import com.surakshasetu.admin.service.StudentLostFoundService;
import org.springframework.web.client.RestClientException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

@Controller
public class StudentPortalController {

    private final StudentRepository studentRepository;
    private final StudentComplaintService studentComplaintService;
    private final DepartmentRepository departmentRepository;
    private final StudentLostFoundService studentLostFoundService;

    @Value("${lostfound.service.url}")
    private String lostFoundServiceUrl;

    public StudentPortalController(
            StudentRepository studentRepository,
            StudentComplaintService studentComplaintService,
            DepartmentRepository departmentRepository,
            StudentLostFoundService studentLostFoundService) {

        this.studentRepository = studentRepository;
        this.studentComplaintService = studentComplaintService;
        this.departmentRepository = departmentRepository;
        this.studentLostFoundService = studentLostFoundService;
    }

    // Student Login
    @GetMapping("/student-portal/login")
    public String studentLogin() {
        return "student-portal/login";
    }

    // Student Logout
    @GetMapping("/student-portal/logout")
    public String studentLogout(HttpSession session) {

        session.invalidate();

        return "redirect:/student-portal/login";
    }

    // Student Dashboard
    @GetMapping("/student-portal/dashboard")
    public String studentDashboard() {
        return "student-portal/dashboard";
    }

    // My Complaints List
    @GetMapping("/student-portal/complaints")
    public String studentComplaints(HttpSession session, Model model) {

        Long studentId = (Long) session.getAttribute("STUDENT_ID");
        System.out.println("Logged-in Student ID: " + studentId);
        
        if (studentId == null) {
            return "redirect:/student-portal/login";
        }

        List<Map<String, Object>> complaints =
                studentComplaintService.getStudentComplaints(studentId);

        model.addAttribute("complaints", complaints);

        return "student-portal/complaints";
    }

    // Register Complaint - Step 1: Department
    @GetMapping("/student-portal/new-complaint")
    public String newComplaint() {
        return "student-portal/new-complaint";
    }

    // Register Complaint - Step 2: Category and Priority
    @GetMapping("/student-portal/new-complaint/details")
    public String complaintDetailsStep2() {
        return "student-portal/complaint-details-step2";
    }

    // Register Complaint - Step 3: Title, Description and Photo
    @GetMapping("/student-portal/new-complaint/description")
    public String complaintDescription() {
        return "student-portal/complaint-description";
    }

    // Register Complaint - Step 4: Review
    @GetMapping("/student-portal/new-complaint/review")
    public String complaintReview() {
        return "student-portal/complaint-review";
    }

    // Complaint Submitted Successfully
    @GetMapping("/student-portal/complaint-success")
    public String complaintSuccess() {
        return "student-portal/complaint-success";
    }

    // Complaint Details - Overview
    @GetMapping("/student-portal/complaints/{id}")
    public String complaintOverview(
            @PathVariable String id,
            HttpSession session,
            Model model) {

        Long studentId = (Long) session.getAttribute("STUDENT_ID");

        if (studentId == null) {
            return "redirect:/student-portal/login";
        }

        try {
            Long complaintId = Long.valueOf(id);

            Map<String, Object> complaint =
                    studentComplaintService.getComplaintById(complaintId);

            Object complaintStudentId = complaint.get("studentId");

            if (!(complaintStudentId instanceof Number)
                    || ((Number) complaintStudentId).longValue() != studentId) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            model.addAttribute("complaint", complaint);

            return "student-portal/complaint-details";

        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    
        // Complaint Status History
        @GetMapping("/student-portal/complaints/{id}/history")
        public String complaintHistory(
                @PathVariable String id,
                HttpSession session,
                Model model) {

            
            System.out.println("HISTORY METHOD CALLED");
            System.out.println("STUDENT SESSION ID: "
                    + session.getAttribute("STUDENT_ID"));
                    
            Long studentId = (Long) session.getAttribute("STUDENT_ID");

            if (studentId == null) {
                return "redirect:/student-portal/login";
            }

            try {
                Long complaintId = Long.valueOf(id);

                // Get complaint details
                Map<String, Object> complaint =
                        studentComplaintService.getComplaintById(complaintId);

                // Verify complaint belongs to logged-in student
                Object complaintStudentId = complaint.get("studentId");

                if (!(complaintStudentId instanceof Number)
                        || ((Number) complaintStudentId).longValue() != studentId) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }

                // Get complaint history
                List<Map<String, Object>> history =
                        studentComplaintService.getComplaintHistory(complaintId);


                        DateTimeFormatter formatter =
                                DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

                        for (Map<String, Object> event : history) {
                            Object changedAt = event.get("changedAt");

                            if (changedAt instanceof String) {
                                try {
                                    LocalDateTime dateTime =
                                            LocalDateTime.parse((String) changedAt);

                                    event.put("changedAtFormatted",
                                            dateTime.format(formatter));

                                } catch (Exception e) {
                                    event.put("changedAtFormatted", changedAt);
                                }
                            } else {
                                event.put("changedAtFormatted", "Date not available");
                            }
                        }

                model.addAttribute("complaint", complaint);
                model.addAttribute("history", history);

                return "student-portal/complaint-history";

            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }


    // Feedback after resolution
    @GetMapping("/student-portal/complaints/{id}/feedback")
    public String complaintFeedback(@PathVariable String id) {
        return "student-portal/complaint-feedback";
    }

    @GetMapping("/student-portal/sos")
    public String studentSos() {
        return "student-portal/sos";
    }


    @GetMapping("/student-portal/lost-found")
    public String studentLostFound(HttpSession session, Model model) {

        Long studentId = (Long) session.getAttribute("STUDENT_ID");

        if (studentId == null) {
            return "redirect:/student-portal/login";
        }

        model.addAttribute("studentId", studentId);

        model.addAttribute("lostFoundServiceUrl", lostFoundServiceUrl);

        try {
            model.addAttribute(
                    "items",
                    studentLostFoundService.getAllItems()
            );
        } catch (RestClientException e) {

            e.printStackTrace(); // Temporary debugging
            
            model.addAttribute("items", List.of());
            model.addAttribute(
                    "lostFoundError",
                    "Lost & Found service is currently unavailable."
            );
        }

        return "student-portal/lost-found";
    }

    @GetMapping("/student-portal/profile")
    public String studentProfile(HttpSession session, Model model) {

        Long studentId = (Long) session.getAttribute("STUDENT_ID");

        if (studentId == null) {
            return "redirect:/student-portal/login";
        }

        Student student = studentRepository.findById(studentId)
                .orElse(null);

        if (student == null) {
            session.invalidate();
            return "redirect:/student-portal/login";
        }

        model.addAttribute("student", student);

        return "student-portal/profile";
    }

    @GetMapping("/student-portal/help")
    public String studentHelp() {
        return "student-portal/help";
    }

    @GetMapping("/student-portal/forgot-password")
    public String studentForgotPassword() {
        return "student-portal/forgot-password";
    }

    @GetMapping("/student-portal/verify-otp")
    public String studentVerifyOtp() {
        return "student-portal/verify-otp";
    }

    @GetMapping("/student-portal/reset-password")
    public String studentResetPassword() {
        return "student-portal/reset-password";
    }

    // Upload Lost & Found Item Image
    @PostMapping(
        value = "/student-portal/lost-found/upload-image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseBody
    public Map<String, Object> uploadLostFoundImage(
            @RequestParam("file") MultipartFile file,
            HttpSession session) {

        Long studentId = (Long) session.getAttribute("STUDENT_ID");

        if (studentId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please login first."
            );
        }

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please select an image."
            );
        }

        try {
            return studentLostFoundService.uploadImage(file);

        } catch (RestClientException e) {

            e.printStackTrace();

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Image upload failed."
            );
        }
    }

    // Submit Lost & Found Item
    @PostMapping(
        value = "/student-portal/lost-found/submit",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Map<String, Object> submitLostFoundItem(
            @RequestBody Map<String, Object> data,
            HttpSession session) {

        Long studentId = (Long) session.getAttribute("STUDENT_ID");

        if (studentId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please login first."
            );
        }

        String itemName = data.get("itemName") == null
                ? null : data.get("itemName").toString().trim();

        String description = data.get("description") == null
                ? null : data.get("description").toString().trim();

        String category = data.get("category") == null
                ? null : data.get("category").toString().trim();

        String type = data.get("type") == null
                ? null : data.get("type").toString().trim().toUpperCase();

        String location = data.get("location") == null
                ? null : data.get("location").toString().trim();

        if (itemName == null || itemName.isBlank()
                || description == null || description.isBlank()
                || category == null || category.isBlank()
                || location == null || location.isBlank()
                || type == null
                || (!type.equals("LOST") && !type.equals("FOUND"))) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please enter valid item details."
            );
        }

        Map<String, Object> itemData = new HashMap<>();

        // Always use logged-in student's ID
        itemData.put("studentId", studentId);
        itemData.put("itemName", itemName);
        itemData.put("description", description);
        itemData.put("category", category);
        itemData.put("type", type);
        itemData.put("location", location);
        itemData.put("photoUrl", data.get("photoUrl"));

        try {
            return studentLostFoundService.createItem(itemData);
        } catch (RestClientException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Unable to submit Lost & Found item."
            );
        }
    }


    // Submit New Complaint
    @PostMapping(
        value = "/student-portal/new-complaint/submit",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Map<String, Object> submitComplaint(
            @RequestBody Map<String, Object> data,
            HttpSession session) {

        Long studentId = (Long) session.getAttribute("STUDENT_ID");

        if (studentId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please login first."
            );
        }

        String departmentName =
                data.get("department") == null
                        ? null : data.get("department").toString().trim();

        String category =
                data.get("category") == null
                        ? null : data.get("category").toString().trim();

        String priority =
                data.get("priority") == null
                        ? null : data.get("priority").toString().trim().toUpperCase();

        String title =
                data.get("title") == null
                        ? null : data.get("title").toString().trim();

        String description =
                data.get("description") == null
                        ? null : data.get("description").toString().trim();

        if (departmentName == null || departmentName.isBlank()
                || category == null || category.isBlank()
                || priority == null || priority.isBlank()
                || title == null || title.isBlank()
                || description == null || description.isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please fill all required complaint details."
            );
        }

        System.out.println("Selected Department from UI: [" + departmentName + "]");

            departmentRepository.findAll().forEach(d ->
                System.out.println("Database Department: [" + d.getDepartmentName() + "]")
            );

        var department = departmentRepository
                .findByDepartmentName(departmentName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Selected department not found."
                ));

        if (!department.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selected department is inactive."
            );
        }

        Map<String, Object> complaintData = new HashMap<>();

        complaintData.put("studentId", studentId);
        complaintData.put("departmentId", department.getId());
        complaintData.put("title", title);
        complaintData.put("description", description);
        complaintData.put("category", category);
        complaintData.put("priority", priority);

        return studentComplaintService.createComplaint(complaintData);
    }

}
