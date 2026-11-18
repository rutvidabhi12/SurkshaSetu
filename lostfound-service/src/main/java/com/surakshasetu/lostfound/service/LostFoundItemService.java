package com.surakshasetu.lostfound.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.surakshasetu.lostfound.entity.LostFoundItem;
import com.surakshasetu.lostfound.enums.ItemStatus;
import com.surakshasetu.lostfound.enums.ItemType;

import java.util.List;

public interface LostFoundItemService {

    LostFoundItem createItem(LostFoundItem item);

    List<LostFoundItem> getAllItems();

    LostFoundItem getItemById(Long id);

    List<LostFoundItem> getItemsByType(ItemType type);

    List<LostFoundItem> getItemsByStatus(ItemStatus status);

    List<LostFoundItem> getItemsByStudentId(Long studentId);

    // Search by Item Name
    List<LostFoundItem> searchItemsByName(String itemName);

    List<LostFoundItem> getItemsByCategory(String category);

    LostFoundItem updateItem(Long id, LostFoundItem item);

    void deleteItem(Long id);

    List<LostFoundItem> getItemsByTypeAndStatus(
        ItemType type,
        ItemStatus status
    );

    Page<LostFoundItem> getAllItems(Pageable pageable);
}
