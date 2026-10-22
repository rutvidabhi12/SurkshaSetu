
package com.surakshasetu.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StudentPortalController {

    // Student Login
    @GetMapping("/student-portal/login")
    public String studentLogin() {
        return "student-portal/login";
    }

    // Student Dashboard
    @GetMapping("/student-portal/dashboard")
    public String studentDashboard() {
        return "student-portal/dashboard";
    }

    // My Complaints List
    @GetMapping("/student-portal/complaints")
    public String studentComplaints() {
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
    public String complaintOverview(@PathVariable String id) {
        return "student-portal/complaint-details";
    }

    // Complaint Status History
    @GetMapping("/student-portal/complaints/{id}/history")
    public String complaintHistory(@PathVariable String id) {
        return "student-portal/complaint-history";
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
    public String studentLostFound() {
        return "student-portal/lost-found";
    }

    @GetMapping("/student-portal/profile")
    public String studentProfile() {
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
}
