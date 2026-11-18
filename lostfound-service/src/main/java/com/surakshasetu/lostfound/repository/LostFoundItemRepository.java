
package com.surakshasetu.lostfound.repository;

import com.surakshasetu.lostfound.entity.LostFoundItem;
import com.surakshasetu.lostfound.enums.ItemStatus;
import com.surakshasetu.lostfound.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface LostFoundItemRepository
        extends JpaRepository<LostFoundItem, Long> {

    List<LostFoundItem> findByType(ItemType type);

    List<LostFoundItem> findByStatus(ItemStatus status);

    List<LostFoundItem> findByStudentId(Long studentId);

    List<LostFoundItem> findByCategoryIgnoreCase(String category);

    List<LostFoundItem> findByTypeAndStatus(
            ItemType type,
            ItemStatus status
    );

    // Search by Item Name
    List<LostFoundItem> findByItemNameContainingIgnoreCase(
            String itemName
    );

    Page<LostFoundItem> findAll(Pageable pageable);
}
