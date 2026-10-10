
package com.surakshasetu.complaints.controller;

import com.surakshasetu.complaints.entity.Complaint;
import com.surakshasetu.complaints.enums.ComplaintStatus;
import com.surakshasetu.complaints.service.ComplaintService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // Create a new complaint
    @PostMapping
    public Complaint createComplaint(@RequestBody Complaint complaint) {
        return complaintService.createComplaint(complaint);
    }

    // Get all complaints
    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    // Get complaint by ID
    @GetMapping("/{id}")
    public Complaint getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintById(id);
    }

    // Get complaints by student ID
    @GetMapping("/student/{studentId}")
    public List<Complaint> getComplaintsByStudent(
            @PathVariable Long studentId) {
        return complaintService.getComplaintsByStudent(studentId);
    }

    // Get complaints assigned to faculty
    @GetMapping("/faculty/{facultyId}")
    public List<Complaint> getComplaintsByFaculty(
            @PathVariable Long facultyId) {
        return complaintService.getComplaintsByFaculty(facultyId);
    }

    // Get complaints by department
    @GetMapping("/department/{departmentId}")
    public List<Complaint> getComplaintsByDepartment(
            @PathVariable Long departmentId) {
        return complaintService.getComplaintsByDepartment(departmentId);
    }

    // Get complaints by status
    @GetMapping("/status/{status}")
    public List<Complaint> getComplaintsByStatus(
            @PathVariable ComplaintStatus status) {
        return complaintService.getComplaintsByStatus(status);
    }

    // Update complaint status
    @PutMapping("/{id}/status")
    public Complaint updateStatus(
            @PathVariable Long id,
            @RequestParam ComplaintStatus status) {
        return complaintService.updateStatus(id, status);
    }
}
