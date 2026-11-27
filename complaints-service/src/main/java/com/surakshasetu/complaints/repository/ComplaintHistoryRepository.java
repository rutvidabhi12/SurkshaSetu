
package com.surakshasetu.complaints.repository;

import com.surakshasetu.complaints.entity.ComplaintHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintHistoryRepository
        extends JpaRepository<ComplaintHistory, Long> {

    List<ComplaintHistory> findByComplaint_IdOrderByChangedAtAsc(Long complaintId);
}
