
package com.surakshasetu.complaints.repository;

import com.surakshasetu.complaints.entity.Complaint;
import com.surakshasetu.complaints.enums.ComplaintStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
        SELECT c FROM Complaint c
        WHERE LOWER(c.category) IN ('campus', 'general')
        ORDER BY c.createdAt DESC
        """)
    List<Complaint> findCampusAndGeneralComplaints();

    // Search all complaints with optional filters and database pagination
    @Query("""
        SELECT c FROM Complaint c
        WHERE (
            :search IS NULL
            OR CAST(c.id AS string) LIKE CONCAT('%', :search, '%')
            OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        AND (:status IS NULL OR c.status = :status)
        AND (
            :category IS NULL
            OR LOWER(c.category) = LOWER(:category)
        )
        """)
    Page<Complaint> searchComplaints(
            @Param("search") String search,
            @Param("status") ComplaintStatus status,
            @Param("category") String category,
            Pageable pageable);
}
