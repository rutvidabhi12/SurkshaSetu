package com.surakshasetu.lostfound.controller;

import com.surakshasetu.lostfound.service.FileStorageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/lost-found")
@CrossOrigin(origins = "*")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file) {

        String imageUrl = fileStorageService.storeFile(file);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Image uploaded successfully",
                        "imageUrl", imageUrl
                )
        );
    }
}