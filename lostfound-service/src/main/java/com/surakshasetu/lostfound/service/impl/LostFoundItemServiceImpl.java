package com.surakshasetu.lostfound.service.impl;

import com.surakshasetu.lostfound.entity.LostFoundItem;
import com.surakshasetu.lostfound.enums.ItemStatus;
import com.surakshasetu.lostfound.enums.ItemType;
import com.surakshasetu.lostfound.repository.LostFoundItemRepository;
import com.surakshasetu.lostfound.service.LostFoundItemService;
import com.surakshasetu.lostfound.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LostFoundItemServiceImpl implements LostFoundItemService {

    private final LostFoundItemRepository repository;

    public LostFoundItemServiceImpl(LostFoundItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public LostFoundItem createItem(LostFoundItem item) {
        return repository.save(item);
    }

    @Override
    public List<LostFoundItem> getAllItems() {
        return repository.findAll();
    }

    @Override
    public LostFoundItem getItemById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lost & Found item not found with ID: " + id
                        )
                );
    }

    @Override
    public List<LostFoundItem> getItemsByType(ItemType type) {
        return repository.findByType(type);
    }

    @Override
    public List<LostFoundItem> getItemsByStatus(ItemStatus status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<LostFoundItem> getItemsByStudentId(Long studentId) {
        return repository.findByStudentId(studentId);
    }

    // Search by Item Name
    @Override
    public List<LostFoundItem> searchItemsByName(String itemName) {
        return repository.findByItemNameContainingIgnoreCase(itemName);
    }

    // Filter by Category
    @Override
    public List<LostFoundItem> getItemsByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category);
    }

    @Override
    public List<LostFoundItem> getItemsByTypeAndStatus(
            ItemType type,
            ItemStatus status) {

        return repository.findByTypeAndStatus(type, status);
    }

    @Override
    public LostFoundItem updateItem(Long id, LostFoundItem item) {
        LostFoundItem existingItem = getItemById(id);

        existingItem.setStudentId(item.getStudentId());
        existingItem.setItemName(item.getItemName());
        existingItem.setDescription(item.getDescription());
        existingItem.setCategory(item.getCategory());
        existingItem.setType(item.getType());
        existingItem.setLocation(item.getLocation());
        existingItem.setPhotoUrl(item.getPhotoUrl());
        existingItem.setStatus(item.getStatus());

        return repository.save(existingItem);
    }

    @Override
    public void deleteItem(Long id) {
        LostFoundItem item = getItemById(id);
        repository.delete(item);
    }

    @Override
    public Page<LostFoundItem> getAllItems(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
