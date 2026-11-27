
package com.surakshasetu.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.client.MultipartBodyBuilder;

@Service
public class StudentLostFoundService {

    private final RestClient lostFoundRestClient;

    public StudentLostFoundService(
            @Qualifier("lostFoundRestClient")
            RestClient lostFoundRestClient) {

        this.lostFoundRestClient = lostFoundRestClient;
    }

    // Get all Lost & Found items
    public List<Map<String, Object>> getAllItems() {

        return lostFoundRestClient.get()
                .uri("/api/lost-found")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    // Get items reported by a particular student
    public List<Map<String, Object>> getStudentItems(Long studentId) {

        return lostFoundRestClient.get()
                .uri("/api/lost-found/student/{studentId}", studentId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    // Create a Lost & Found item
    public Map<String, Object> createItem(
            Map<String, Object> itemData) {

        return lostFoundRestClient.post()
                .uri("/api/lost-found")
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemData)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    // Upload item image
    public Map<String, Object> uploadImage(MultipartFile file) {

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());

        MultiValueMap<String, org.springframework.http.HttpEntity<?>> parts =
                builder.build();

        return lostFoundRestClient.post()
                .uri("/api/lost-found/upload-image")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(parts)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}
