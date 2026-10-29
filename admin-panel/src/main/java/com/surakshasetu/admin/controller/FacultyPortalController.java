package com.surakshasetu.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FacultyPortalController {

    @GetMapping("/faculty-portal/login")
    public String facultyLogin() {
        return "faculty-portal/login";
    }

     // Faculty Dashboard Page
    @GetMapping("/faculty-portal/dashboard")
    public String facultyDashboard() {
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
    public String facultyProfile() {
        return "faculty-portal/profile";
    }

    @GetMapping("/faculty-portal/lost-found")
    public String facultyLostFound() {
        return "faculty-portal/lost-found";
    }

    @GetMapping("/faculty-portal/forgot-password")
    public String forgotPassword() {
        return "faculty-portal/forgot-password";
    }
}


