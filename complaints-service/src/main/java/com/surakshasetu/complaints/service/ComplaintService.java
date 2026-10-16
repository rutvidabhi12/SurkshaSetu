
package com.surakshasetu.complaints.service;

import com.surakshasetu.complaints.entity.Complaint;
import com.surakshasetu.complaints.enums.ComplaintStatus;
import com.surakshasetu.complaints.repository.ComplaintRepository;
import org.springframework.stereotype.Service;
import com.surakshasetu.complaints.repository.DepartmentMappingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final DepartmentMappingRepository departmentMappingRepository;

    public ComplaintService(
            ComplaintRepository complaintRepository,
            DepartmentMappingRepository departmentMappingRepository) {
        this.complaintRepository = complaintRepository;
        this.departmentMappingRepository = departmentMappingRepository;
    }

    public Complaint createComplaint(Complaint complaint) {

    // Default status
    complaint.setStatus(ComplaintStatus.PENDING);

    // Campus/General complaints are handled by Admin.
    String category = complaint.getCategory();

    if (category != null &&
            (category.equalsIgnoreCase("Campus")
                    || category.equalsIgnoreCase("General"))) {
        complaint.setAssignedFacultyId(null);
        return complaintRepository.save(complaint);
    }

    // Find fixed faculty for the selected department.
    if (complaint.getDepartmentId() != null) {
        departmentMappingRepository
                .findFixedFacultyId(complaint.getDepartmentId())
                .ifPresent(facultyId -> {
                    complaint.setAssignedFacultyId(facultyId);
                    complaint.setStatus(ComplaintStatus.ASSIGNED);
                });
    }

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

    public List<Complaint> getCampusAndGeneralComplaints() {
        return complaintRepository.findCampusAndGeneralComplaints();
    }

    
    public Complaint updateComplaintDetails(
            Long id,
            ComplaintStatus status,
            String resolutionNote,
            String rejectionReason) {

        Complaint complaint = getComplaintById(id);

        if (status == ComplaintStatus.RESOLVED) {
            if (resolutionNote == null || resolutionNote.isBlank()) {
                throw new IllegalArgumentException(
                        "Resolution note is required when resolving a complaint");
            }

            complaint.setResolutionNote(resolutionNote.trim());
            complaint.setRejectionReason(null);

        } else if (status == ComplaintStatus.REJECTED) {
            if (rejectionReason == null || rejectionReason.isBlank()) {
                throw new IllegalArgumentException(
                        "Rejection reason is required when rejecting a complaint");
            }

            complaint.setRejectionReason(rejectionReason.trim());
            complaint.setResolutionNote(null);

        } else {
            complaint.setResolutionNote(null);
            complaint.setRejectionReason(null);
        }

        complaint.setStatus(status);

        return complaintRepository.save(complaint);
    }

    public Page<Complaint> searchComplaints(
        String search,
        ComplaintStatus status,
        String category,
        Pageable pageable) {

    if (search != null && search.isBlank()) {
        search = null;
    }

    if (category != null && category.isBlank()) {
        category = null;
    }

    return complaintRepository.searchComplaints(
            search,
            status,
            category,
            pageable
    );
}

}
