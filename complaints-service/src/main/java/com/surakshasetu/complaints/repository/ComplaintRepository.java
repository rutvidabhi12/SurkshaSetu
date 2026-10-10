
package com.surakshasetu.complaints.repository;

import com.surakshasetu.complaints.entity.Complaint;
import com.surakshasetu.complaints.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository
        extends JpaRepository<Complaint, Long> {

    List<Complaint> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    List<Complaint> findByAssignedFacultyIdOrderByCreatedAtDesc(
            Long facultyId);

    List<Complaint> findByStatusOrderByCreatedAtDesc(
            ComplaintStatus status);

    List<Complaint> findByDepartmentIdOrderByCreatedAtDesc(
            Long departmentId);
}
