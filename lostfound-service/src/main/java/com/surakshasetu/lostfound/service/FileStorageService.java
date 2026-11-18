
package com.surakshasetu.lostfound.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.Set;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".webp"
    );

    public String storeFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select an image"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "Image size must not exceed 5 MB"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Only JPG, PNG and WEBP images are allowed"
            );
        }

        try {
            String originalName = file.getOriginalFilename();

            if (originalName == null ||
                    !originalName.contains(".")) {
                throw new IllegalArgumentException(
                        "Invalid image file name"
                );
            }

            String extension = originalName.substring(
                    originalName.lastIndexOf(".")
            ).toLowerCase();

            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new IllegalArgumentException(
                        "Invalid image extension"
                );
            }

            byte[] fileBytes = file.getBytes();

            String actualType = detectImageType(fileBytes);

            if (actualType == null) {
                throw new IllegalArgumentException(
                        "Invalid image file content"
                );
            }

            if (!matchesExtension(extension, actualType)) {
                throw new IllegalArgumentException(
                        "Image content does not match file extension"
                );
            }

            Path uploadPath = Paths.get(uploadDir)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + extension;

            Path targetPath = uploadPath.resolve(fileName)
                    .normalize();

            if (!targetPath.startsWith(uploadPath)) {
                throw new IllegalArgumentException(
                        "Invalid file path"
                );
            }

            Files.write(targetPath, fileBytes);

            return "/uploads/" + fileName;

        } catch (IOException ex) {
            throw new RuntimeException(
                    "Could not store image", ex
            );
        }
    }

    private String detectImageType(byte[] bytes) {

        // JPEG signature
        if (bytes.length >= 3 &&
                (bytes[0] & 0xFF) == 0xFF &&
                (bytes[1] & 0xFF) == 0xD8 &&
                (bytes[2] & 0xFF) == 0xFF) {
            return "jpeg";
        }

        // PNG signature
        if (bytes.length >= 8 &&
                (bytes[0] & 0xFF) == 0x89 &&
                bytes[1] == 0x50 &&
                bytes[2] == 0x4E &&
                bytes[3] == 0x47 &&
                bytes[4] == 0x0D &&
                bytes[5] == 0x0A &&
                bytes[6] == 0x1A &&
                bytes[7] == 0x0A) {
            return "png";
        }

        // WEBP signature
        if (bytes.length >= 12 &&
                bytes[0] == 'R' &&
                bytes[1] == 'I' &&
                bytes[2] == 'F' &&
                bytes[3] == 'F' &&
                bytes[8] == 'W' &&
                bytes[9] == 'E' &&
                bytes[10] == 'B' &&
                bytes[11] == 'P') {
            return "webp";
        }

        return null;
    }

    private boolean matchesExtension(
            String extension, String actualType) {

        if (actualType.equals("jpeg")) {
            return extension.equals(".jpg") ||
                    extension.equals(".jpeg");
        }

        if (actualType.equals("png")) {
            return extension.equals(".png");
        }

        if (actualType.equals("webp")) {
            return extension.equals(".webp");
        }

        return false;
    }
}
