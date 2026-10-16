
package com.surakshasetu.complaints.controller;

import com.surakshasetu.complaints.entity.Complaint;
import com.surakshasetu.complaints.enums.ComplaintStatus;
import com.surakshasetu.complaints.service.ComplaintService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

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

    // Get Campus and General complaints for Admin handling
    @GetMapping("/admin")
    public List<Complaint> getCampusAndGeneralComplaints() {
        return complaintService.getCampusAndGeneralComplaints();
    }

    
    
    // Update complaint status with resolution or rejection details
    @PutMapping("/{id}/details")
    public Complaint updateComplaintDetails(
            @PathVariable Long id,
            @RequestParam ComplaintStatus status,
            @RequestParam(required = false) String resolutionNote,
            @RequestParam(required = false) String rejectionReason) {

        // Validate resolution note
        if (status == ComplaintStatus.RESOLVED
                && (resolutionNote == null || resolutionNote.isBlank())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Resolution note is required when resolving a complaint."
            );
        }

        // Validate rejection reason
        if (status == ComplaintStatus.REJECTED
                && (rejectionReason == null || rejectionReason.isBlank())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rejection reason is required when rejecting a complaint."
            );
        }

        return complaintService.updateComplaintDetails(
                id,
                status,
                resolutionNote,
                rejectionReason
        );
    }

    
@GetMapping("/search")
public Page<Complaint> searchComplaints(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) ComplaintStatus status,
        @RequestParam(required = false) String category,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    if (page < 0 || size < 1 || size > 100) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid page or size. Size must be between 1 and 100."
        );
    }

    Pageable pageable = PageRequest.of(page, size);

    return complaintService.searchComplaints(
            search,
            status,
            category,
            pageable
    );
}


}
