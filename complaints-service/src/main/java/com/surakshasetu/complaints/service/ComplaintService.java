
package com.surakshasetu.complaints.service;

import com.surakshasetu.complaints.entity.Complaint;
import com.surakshasetu.complaints.entity.ComplaintHistory;
import com.surakshasetu.complaints.enums.ComplaintStatus;
import com.surakshasetu.complaints.repository.ComplaintRepository;
import com.surakshasetu.complaints.repository.ComplaintHistoryRepository;
import com.surakshasetu.complaints.repository.DepartmentMappingRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final DepartmentMappingRepository departmentMappingRepository;
    private final ComplaintHistoryRepository complaintHistoryRepository;

    public ComplaintService(
            ComplaintRepository complaintRepository,
            DepartmentMappingRepository departmentMappingRepository,
            ComplaintHistoryRepository complaintHistoryRepository) {

        this.complaintRepository = complaintRepository;
        this.departmentMappingRepository = departmentMappingRepository;
        this.complaintHistoryRepository = complaintHistoryRepository;
    }

    private void saveHistory(
            Complaint complaint,
            ComplaintStatus previousStatus,
            ComplaintStatus newStatus,
            String remarks) {

        ComplaintHistory history = new ComplaintHistory();
        history.setComplaint(complaint);
        history.setPreviousStatus(previousStatus);
        history.setNewStatus(newStatus);
        history.setRemarks(remarks);

        complaintHistoryRepository.save(history);
    }

    @Transactional
    public Complaint createComplaint(Complaint complaint) {

        complaint.setStatus(ComplaintStatus.PENDING);

        String category = complaint.getCategory();

        if (category != null &&
                (category.equalsIgnoreCase("Campus")
                        || category.equalsIgnoreCase("General"))) {

            complaint.setAssignedFacultyId(null);

        } else if (complaint.getDepartmentId() != null) {

            departmentMappingRepository
                    .findFixedFacultyId(complaint.getDepartmentId())
                    .ifPresent(facultyId -> {
                        complaint.setAssignedFacultyId(facultyId);
                        complaint.setStatus(ComplaintStatus.ASSIGNED);
                    });
        }

        Complaint savedComplaint = complaintRepository.save(complaint);

        saveHistory(
                savedComplaint,
                null,
                savedComplaint.getStatus(),
                "Complaint submitted"
        );

        return savedComplaint;
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

    @Transactional
    public Complaint updateStatus(Long id, ComplaintStatus status) {

        Complaint complaint = getComplaintById(id);
        ComplaintStatus previousStatus = complaint.getStatus();

        complaint.setStatus(status);

        Complaint savedComplaint = complaintRepository.save(complaint);

        if (previousStatus != status) {
            saveHistory(
                    savedComplaint,
                    previousStatus,
                    status,
                    "Complaint status updated"
            );
        }

        return savedComplaint;
    }

    public List<Complaint> getCampusAndGeneralComplaints() {
        return complaintRepository.findCampusAndGeneralComplaints();
    }

    @Transactional
    public Complaint updateComplaintDetails(
            Long id,
            ComplaintStatus status,
            String resolutionNote,
            String rejectionReason) {

        Complaint complaint = getComplaintById(id);
        ComplaintStatus previousStatus = complaint.getStatus();

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

        Complaint savedComplaint = complaintRepository.save(complaint);

        if (previousStatus != status) {

            String remarks = null;

            if (status == ComplaintStatus.RESOLVED) {
                remarks = resolutionNote.trim();
            } else if (status == ComplaintStatus.REJECTED) {
                remarks = rejectionReason.trim();
            }

            saveHistory(
                    savedComplaint,
                    previousStatus,
                    status,
                    remarks
            );
        }

        return savedComplaint;
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

    public List<ComplaintHistory> getComplaintHistory(Long complaintId) {

        // Check whether complaint exists
        getComplaintById(complaintId);

        // Return history in chronological order
        return complaintHistoryRepository
                .findByComplaint_IdOrderByChangedAtAsc(complaintId);
    }

}
