
package com.surakshasetu.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.surakshasetu.admin.security.FacultyRequestSigner;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import java.util.List;
import com.surakshasetu.admin.entity.Student;
import com.surakshasetu.admin.service.StudentService;
import java.util.HashMap;

import java.util.Map;

@Controller
public class FacultyPortalController {

    @Value("${faculty.service.url:http://localhost:8082}")
    private String facultyServiceUrl;

    @Value("${lostfound.service.url:http://localhost:8085}")
    private String lostFoundServiceUrl;

    @Value("${complaints.service.url}")
    private String complaintsServiceUrl;

   

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private FacultyRequestSigner facultyRequestSigner;

     @Autowired
    private StudentService studentService;

    @GetMapping("/faculty-portal/login")
    public String facultyLogin() {
        return "faculty-portal/login";
    }

    @PostMapping("/faculty-portal/login")
    public String processFacultyLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {

        try {
            Map<String, String> loginData = Map.of(
                    "email", email,
                    "password", password
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity =
                    new HttpEntity<>(loginData, headers);

            ResponseEntity<Map> apiResponse = restTemplate.postForEntity(
                    facultyServiceUrl + "/api/faculty/login",
                    entity,
                    Map.class
            );

            Map<String, Object> responseBody = apiResponse.getBody();

            if (responseBody == null || !responseBody.containsKey("faculty")) {
                model.addAttribute("loginError", "Invalid email or password.");
                return "faculty-portal/login";
            }

            Map<String, Object> faculty =
                    (Map<String, Object>) responseBody.get("faculty");

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_FACULTY"))
                    );

            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            HttpSession session = request.getSession(true);
            session.setAttribute("faculty", faculty);

            new HttpSessionSecurityContextRepository()
                    .saveContext(context, request, response);

            return "redirect:/faculty-portal/dashboard";

        } catch (RestClientException e) {
            model.addAttribute(
                    "loginError",
                    "Login failed. Please check your email and password."
            );
            return "faculty-portal/login";
        }
    }


    @PostMapping("/faculty-portal/logout")
        public String facultyLogout(
                HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication) {

        new SecurityContextLogoutHandler()
                .logout(request, response, authentication);

        return "redirect:/faculty-portal/login?logout";
        }

    @GetMapping("/faculty-portal/dashboard")
    public String facultyDashboard(HttpSession session, Model model) {

        Object facultyObject = session.getAttribute("faculty");

        if (!(facultyObject instanceof Map)) {
            return "redirect:/faculty-portal/login";
        }

        Map<String, Object> faculty =
                (Map<String, Object>) facultyObject;

        Object idObject = faculty.get("id");

        if (!(idObject instanceof Number)) {
            return "redirect:/faculty-portal/login";
        }

        Long facultyId = ((Number) idObject).longValue();

        String firstName = String.valueOf(
                faculty.getOrDefault("firstName", "")
        );

        String middleName = String.valueOf(
                faculty.getOrDefault("middleName", "")
        );

        String lastName = String.valueOf(
                faculty.getOrDefault("lastName", "")
        );

        String facultyName = (firstName + " " + middleName + " " + lastName)
                .replaceAll("\\s+", " ")
                .trim();

        String department = String.valueOf(
                faculty.getOrDefault("department", "")
        );

        List<Map<String, Object>> complaints = new ArrayList<>();

        try {
            String requestUri =
        "/api/complaints/faculty/" + facultyId;

String apiUrl = complaintsServiceUrl + requestUri;

HttpHeaders apiHeaders = facultyRequestSigner.createHeaders(
        facultyId,
        "GET",
        requestUri
);

HttpEntity<Void> apiEntity = new HttpEntity<>(apiHeaders);

ResponseEntity<List> apiResult = restTemplate.exchange(
        URI.create(apiUrl),
        org.springframework.http.HttpMethod.GET,
        apiEntity,
        List.class
);

List<?> apiResponse = apiResult.getBody();

            if (apiResponse != null) {
                for (Object item : apiResponse) {
                    if (item instanceof Map<?, ?>) {
                        Map<String, Object> complaint = new java.util.HashMap<>();

                        ((Map<?, ?>) item).forEach((key, value) ->
                                complaint.put(String.valueOf(key), value)
                        );

                        complaints.add(complaint);
                    }
                }
            }

        } catch (RestClientException e) {
            model.addAttribute(
                    "dashboardError",
                    "Unable to load complaints. Please try again."
            );
        }

        long progressCount = complaints.stream()
                .filter(c -> "IN_PROGRESS".equalsIgnoreCase(
                        String.valueOf(c.get("status"))))
                .count();

        long resolvedCount = complaints.stream()
                .filter(c -> "RESOLVED".equalsIgnoreCase(
                        String.valueOf(c.get("status"))))
                .count();

        long rejectedCount = complaints.stream()
                .filter(c -> "REJECTED".equalsIgnoreCase(
                        String.valueOf(c.get("status"))))
                .count();

        List<Map<String, Object>> recentComplaints =
                complaints.size() > 5
                        ? new ArrayList<>(complaints.subList(0, 5))
                        : complaints;

        model.addAttribute("facultyName", facultyName);
        model.addAttribute("facultyDepartment", department);

        model.addAttribute("assignedCount", complaints.size());
        model.addAttribute("progressCount", progressCount);
        model.addAttribute("resolvedCount", resolvedCount);
        model.addAttribute("rejectedCount", rejectedCount);

        model.addAttribute("recentComplaints", recentComplaints);

        return "faculty-portal/dashboard";
    }

    @GetMapping("/faculty-portal/complaints")
    public String facultyComplaints() {
        return "faculty-portal/complaints";
    }

    @GetMapping("/faculty-portal/complaint-details")
    public String facultyComplaintDetails() {
        return "faculty-portal/complaint-details";
    }

    @GetMapping("/faculty-portal/update-complaint")
    public String updateFacultyComplaint() {
        return "faculty-portal/update-complaint";
    }

    
@GetMapping("/faculty-portal/profile")
public String facultyProfile(HttpSession session, Model model) {

    Object facultyObject = session.getAttribute("faculty");

    if (!(facultyObject instanceof Map)) {
        return "redirect:/faculty-portal/login";
    }

    Map<String, Object> faculty =
            (Map<String, Object>) facultyObject;

    String firstName = String.valueOf(
            faculty.getOrDefault("firstName", "")
    );

    String middleName = String.valueOf(
            faculty.getOrDefault("middleName", "")
    );

    String lastName = String.valueOf(
            faculty.getOrDefault("lastName", "")
    );

    String facultyName = (firstName + " " + middleName + " " + lastName)
            .replaceAll("\\s+", " ")
            .trim();

    model.addAttribute("facultyName", facultyName);
    model.addAttribute("facultyCode",
            faculty.get("facultyCode") != null
                    ? faculty.get("facultyCode") : "Not available");

    model.addAttribute("facultyEmail",
            faculty.get("email") != null
                    ? faculty.get("email") : "Not available");

    model.addAttribute("facultyMobile",
            faculty.get("mobile") != null
                    ? faculty.get("mobile") : "Not available");

    model.addAttribute("facultyDepartment",
            faculty.get("department") != null
                    ? faculty.get("department") : "Not available");

    model.addAttribute("facultyDesignation",
            faculty.get("designation") != null
                    ? faculty.get("designation") : "Not available");

    return "faculty-portal/profile";
}


    @GetMapping("/faculty-portal/lost-found")
public String facultyLostFound(Model model) {

    String apiUrl = lostFoundServiceUrl + "/api/lost-found";

    try {

        List<?> items = restTemplate.getForObject(
                apiUrl,
                List.class
        );

        List<Map<String, Object>> updatedItems = new ArrayList<>();

        if (items != null) {

            for (Object item : items) {

                if (item instanceof Map<?, ?>) {

                    Map<String, Object> itemMap = new HashMap<>();

                    ((Map<?, ?>) item).forEach((key, value) ->
                            itemMap.put(String.valueOf(key), value)
                    );

                    Object studentIdObject = itemMap.get("studentId");

                    String studentName = "Student";

                    if (studentIdObject != null) {

                        try {

                            Long studentId = Long.valueOf(
                                    studentIdObject.toString()
                            );

                            Student student =
                                    studentService.getStudentById(studentId);

                            if (student != null) {

                                studentName = (
                                        student.getFirstName() + " " +
                                        (student.getMiddleName() != null
                                                ? student.getMiddleName() + " "
                                                : "") +
                                        student.getLastName()
                                ).replaceAll("\\s+", " ").trim();
                            }

                        } catch (Exception e) {
                            System.out.println(
                                    "Student not found for ID: " +
                                    studentIdObject
                            );
                        }
                    }

                    itemMap.put("studentName", studentName);

                    updatedItems.add(itemMap);
                }
            }
        }

        model.addAttribute("lostFoundItems", updatedItems);

    } catch (RestClientException e) {

        model.addAttribute(
                "lostFoundItems",
                Collections.emptyList()
        );

        model.addAttribute(
                "lostFoundError",
                "Unable to load Lost & Found items."
        );
    }

    return "faculty-portal/lost-found";
}

    @GetMapping("/faculty-portal/forgot-password")
    public String forgotPassword() {
        return "faculty-portal/forgot-password";
    }
}
