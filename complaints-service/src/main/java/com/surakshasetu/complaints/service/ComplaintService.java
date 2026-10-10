
package com.surakshasetu.complaints.service;

import com.surakshasetu.complaints.entity.Complaint;
import com.surakshasetu.complaints.enums.ComplaintStatus;
import com.surakshasetu.complaints.repository.ComplaintRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public Complaint createComplaint(Complaint complaint) {
        complaint.setStatus(ComplaintStatus.PENDING);
        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Complaint not found with id: " + id));
    }

    public List<Complaint> getComplaintsByStudent(Long studentId) {
        return complaintRepository
                .findByStudentIdOrderByCreatedAtDesc(studentId);
    }

    public List<Complaint> getComplaintsByFaculty(Long facultyId) {
        return complaintRepository
                .findByAssignedFacultyIdOrderByCreatedAtDesc(facultyId);
    }

    public List<Complaint> getComplaintsByDepartment(Long departmentId) {
        return complaintRepository
                .findByDepartmentIdOrderByCreatedAtDesc(departmentId);
    }

    public List<Complaint> getComplaintsByStatus(ComplaintStatus status) {
        return complaintRepository
                .findByStatusOrderByCreatedAtDesc(status);
    }

    public Complaint updateStatus(Long id, ComplaintStatus status) {
        Complaint complaint = getComplaintById(id);
        complaint.setStatus(status);
        return complaintRepository.save(complaint);
    }
}
