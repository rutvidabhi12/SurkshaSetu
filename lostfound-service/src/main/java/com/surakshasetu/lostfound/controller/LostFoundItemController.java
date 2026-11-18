
package com.surakshasetu.lostfound.controller;

import com.surakshasetu.lostfound.entity.LostFoundItem;
import com.surakshasetu.lostfound.enums.ItemStatus;
import com.surakshasetu.lostfound.enums.ItemType;
import com.surakshasetu.lostfound.service.LostFoundItemService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RestController
@RequestMapping("/api/lost-found")
@CrossOrigin(origins = "*")
public class LostFoundItemController {

    private final LostFoundItemService service;

    public LostFoundItemController(LostFoundItemService service) {
        this.service = service;
    }

    // Create Item
    @PostMapping
    public ResponseEntity<LostFoundItem> createItem(
            @Valid @RequestBody LostFoundItem item) {

        return new ResponseEntity<>(
                service.createItem(item),
                HttpStatus.CREATED
        );
    }

    // Get All Items
    @GetMapping
    public ResponseEntity<List<LostFoundItem>> getAllItems() {
        return ResponseEntity.ok(service.getAllItems());
    }

    // Get Item By ID
    @GetMapping("/{id}")
    public ResponseEntity<LostFoundItem> getItemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getItemById(id));
    }

    // Get Items By Type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<LostFoundItem>> getItemsByType(
            @PathVariable ItemType type) {

        return ResponseEntity.ok(service.getItemsByType(type));
    }

    // Get Items By Status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LostFoundItem>> getItemsByStatus(
            @PathVariable ItemStatus status) {

        return ResponseEntity.ok(service.getItemsByStatus(status));
    }

    // Get Items By Student ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<LostFoundItem>> getItemsByStudentId(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                service.getItemsByStudentId(studentId)
        );
    }

    // Search Items By Name
    @GetMapping("/search")
    public ResponseEntity<List<LostFoundItem>> searchItems(
            @RequestParam String name) {

        return ResponseEntity.ok(
                service.searchItemsByName(name)
        );
    }

    // Filter By Type And Status
    @GetMapping("/filter")
    public ResponseEntity<List<LostFoundItem>> getItemsByTypeAndStatus(
            @RequestParam ItemType type,
            @RequestParam ItemStatus status) {

        return ResponseEntity.ok(
                service.getItemsByTypeAndStatus(type, status)
        );
    }

    // Filter By Category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<LostFoundItem>> getItemsByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(
                service.getItemsByCategory(category)
        );
    }

    // Update Item
    @PutMapping("/{id}")
    public ResponseEntity<LostFoundItem> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody LostFoundItem item) {

        return ResponseEntity.ok(
                service.updateItem(id, item)
        );
    }

    // Delete Item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id) {

        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    // Pagination
    @GetMapping("/page")
    public ResponseEntity<Page<LostFoundItem>> getAllItemsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                service.getAllItems(pageable)
        );
    }
}
