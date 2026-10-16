
package com.surakshasetu.admin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ComplaintAdminController {

    @Value("${complaints.service.url}")
    private String complaintsServiceUrl;

    private final RestClient restClient = RestClient.create();


    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get all complaints and display error message if present
    
@GetMapping("/complaints")
public String showComplaints(
        Model model,
        @RequestParam(required = false) String error,
        @RequestParam(required = false) String success,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String category,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    UriComponentsBuilder builder = UriComponentsBuilder
            .fromUriString(complaintsServiceUrl + "/api/complaints/search")
            .queryParam("page", page)
            .queryParam("size", size);

    if (search != null && !search.isBlank()) {
        builder.queryParam("search", search.trim());
    }

    if (status != null && !status.isBlank()) {
        builder.queryParam("status", status);
    }

    if (category != null && !category.isBlank()) {
        builder.queryParam("category", category);
    }

    Map<String, Object> response = restClient.get()
            .uri(builder.build().encode().toUri())
            .retrieve()
            .body(new org.springframework.core.ParameterizedTypeReference<
                    Map<String, Object>>() {});

    List<Map<String, Object>> complaints =
        response != null && response.get("content") != null
                ? (List<Map<String, Object>>) response.get("content")
                : new ArrayList<>();

// Add Department Name and Faculty Name
for (Map<String, Object> complaint : complaints) {

    // Department Name
    Object departmentId = complaint.get("departmentId");
    String departmentName = "Not Available";

    if (departmentId != null) {
        List<Map<String, Object>> departments =
                jdbcTemplate.queryForList(
                        "SELECT department_name FROM departments WHERE id = ?",
                        departmentId
                );

        if (!departments.isEmpty()) {
            departmentName = String.valueOf(
                    departments.get(0).get("department_name"));
        }
    }

    complaint.put("departmentName", departmentName);

    // Faculty Name
    Object facultyId = complaint.get("assignedFacultyId");
    String facultyName = "Not Assigned";

    if (facultyId != null) {
        List<Map<String, Object>> faculties =
                jdbcTemplate.queryForList(
                        "SELECT first_name, middle_name, last_name FROM faculty WHERE id = ?",
                        facultyId
                );

        if (!faculties.isEmpty()) {
            facultyName = buildFullName(faculties.get(0));
        }
    }

    complaint.put("facultyName", facultyName);
}

model.addAttribute("complaints", complaints);

    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages",
            response != null ? response.get("totalPages") : 0);
    model.addAttribute("search", search);
    model.addAttribute("status", status);
    model.addAttribute("category", category);

    if (error != null && !error.isBlank()) {
        model.addAttribute("error", error);
    }

    if (success != null && !success.isBlank()) {
        model.addAttribute("success", success);
    }

    return "complaint/index";
}


    // Get Campus and General complaints for Admin handling
    @GetMapping("/complaints/admin")
    public String showAdminComplaints(Model model) {

        List<Map<String, Object>> complaints =
                restClient.get()
                        .uri(complaintsServiceUrl + "/api/complaints/admin")
                        .retrieve()
                        .body(new org.springframework.core.ParameterizedTypeReference<
                                List<Map<String, Object>>>() {});

        model.addAttribute("complaints",
                complaints != null ? complaints : List.of());

        return "complaint/index";
    }

    

@GetMapping("/complaints/{id}")
public String showComplaintDetails(
        @PathVariable Long id,
        Model model) {

    Map<String, Object> complaint =
            restClient.get()
                    .uri(complaintsServiceUrl + "/api/complaints/" + id)
                    .retrieve()
                    .body(new org.springframework.core.ParameterizedTypeReference<
                            Map<String, Object>>() {});

    if (complaint == null) {
        return "redirect:/complaints?error=Complaint%20not%20found";
    }

    // Student Name
    Object studentId = complaint.get("studentId");
    String studentName = "";

    if (studentId != null) {
        List<Map<String, Object>> students = jdbcTemplate.queryForList(
                "SELECT first_name, middle_name, last_name FROM students WHERE id = ?",
                studentId
        );

        if (!students.isEmpty()) {
            Map<String, Object> student = students.get(0);
            studentName = buildFullName(student);
        }
    }

    // Department Name
    Object departmentId = complaint.get("departmentId");
    String departmentName = "";

    if (departmentId != null) {
        List<Map<String, Object>> departments = jdbcTemplate.queryForList(
                "SELECT department_name FROM departments WHERE id = ?",
                departmentId
        );

        if (!departments.isEmpty()) {
            departmentName = String.valueOf(
                    departments.get(0).get("department_name"));
        }
    }

    // Faculty Name
    Object facultyId = complaint.get("assignedFacultyId");
    String facultyName = "Not Assigned";

    if (facultyId != null) {
        List<Map<String, Object>> faculties = jdbcTemplate.queryForList(
                "SELECT first_name, middle_name, last_name FROM faculty WHERE id = ?",
                facultyId
        );

        if (!faculties.isEmpty()) {
            facultyName = buildFullName(faculties.get(0));
        }
    }
// Format Created At
Object createdAt = complaint.get("createdAt");

if (createdAt != null) {
    try {
        java.time.LocalDateTime dateTime =
                java.time.LocalDateTime.parse(createdAt.toString());

        complaint.put("createdAtFormatted",
                dateTime.format(
                        java.time.format.DateTimeFormatter.ofPattern(
                                "dd MMM yyyy, hh:mm a",
                                java.util.Locale.ENGLISH
                        )
                ));
    } catch (java.time.format.DateTimeParseException e) {
        complaint.put("createdAtFormatted", createdAt.toString());
    }
}

// Format Last Updated
Object updatedAt = complaint.get("updatedAt");

if (updatedAt != null) {
    try {
        java.time.LocalDateTime dateTime =
                java.time.LocalDateTime.parse(updatedAt.toString());

        complaint.put("updatedAtFormatted",
                dateTime.format(
                        java.time.format.DateTimeFormatter.ofPattern(
                                "dd MMM yyyy, hh:mm a",
                                java.util.Locale.ENGLISH
                        )
                ));
    } catch (java.time.format.DateTimeParseException e) {
        complaint.put("updatedAtFormatted", updatedAt.toString());
    }
}


    model.addAttribute("complaint", complaint);
    model.addAttribute("studentName",
            studentName.isBlank() ? "Not Available" : studentName);
    model.addAttribute("departmentName",
            departmentName.isBlank() ? "Not Available" : departmentName);
    model.addAttribute("facultyName", facultyName);

    return "complaint/details";
}

private String buildFullName(Map<String, Object> person) {
    return java.util.stream.Stream.of(
                    person.get("first_name"),
                    person.get("middle_name"),
                    person.get("last_name"))
            .filter(value -> value != null
                    && !value.toString().isBlank())
            .map(Object::toString)
            .collect(java.util.stream.Collectors.joining(" "));
}



// Update complaint status with validation and feedback
@PostMapping("/complaints/{id}/update")
public String updateComplaint(
        @PathVariable Long id,
        @RequestParam String status,
        @RequestParam(required = false) String resolutionNote,
        @RequestParam(required = false) String rejectionReason) {

    if ("RESOLVED".equals(status)
            && (resolutionNote == null || resolutionNote.isBlank())) {
        return "redirect:/complaints?error="
                + java.net.URLEncoder.encode(
                        "Resolution note is required",
                        java.nio.charset.StandardCharsets.UTF_8);
    }

    if ("REJECTED".equals(status)
            && (rejectionReason == null || rejectionReason.isBlank())) {
        return "redirect:/complaints?error="
                + java.net.URLEncoder.encode(
                        "Rejection reason is required",
                        java.nio.charset.StandardCharsets.UTF_8);
    }

    try {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(
                        complaintsServiceUrl
                                + "/api/complaints/" + id + "/details")
                .queryParam("status", status);

        if (resolutionNote != null && !resolutionNote.isBlank()) {
            builder.queryParam("resolutionNote", resolutionNote.trim());
        }

        if (rejectionReason != null && !rejectionReason.isBlank()) {
            builder.queryParam("rejectionReason", rejectionReason.trim());
        }

        restClient.put()
                .uri(builder.build().encode().toUri())
                .retrieve()
                .body(Map.class);

        return "redirect:/complaints?success="
                + java.net.URLEncoder.encode(
                        "Complaint updated successfully!",
                        java.nio.charset.StandardCharsets.UTF_8);

    } catch (org.springframework.web.client.RestClientResponseException ex) {

        String message =
                "Complaint update failed. Please check the details and try again.";

        if (ex.getStatusCode().value() == 400) {
            message = "Invalid complaint details. Please check the required fields.";
        }

        return "redirect:/complaints?error="
                + java.net.URLEncoder.encode(
                        message,
                        java.nio.charset.StandardCharsets.UTF_8);
    }
}
}
