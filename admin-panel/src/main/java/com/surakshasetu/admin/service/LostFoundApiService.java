
package com.surakshasetu.admin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class LostFoundApiService {

    private final RestClient restClient;

    public LostFoundApiService(
            @Value("${lostfound.service.base-url}") String baseUrl,
            @Value("${lostfound.service.username}") String username,
            @Value("${lostfound.service.password}") String password) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(
                        username, password))
                .defaultHeader(
                        HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // Get All Items
    public List<Map<String, Object>> getAllItems() {
        return restClient.get()
                .uri("/api/lost-found")
                .retrieve()
                .body(new ParameterizedTypeReference<
                        List<Map<String, Object>>>() {});
    }

    // Update Item
    public Map<String, Object> updateItem(
            Long id, Map<String, Object> item) {

        return restClient.put()
                .uri("/api/lost-found/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(item)
                .retrieve()
                .body(new ParameterizedTypeReference<
                        Map<String, Object>>() {});
    }

    // Delete Item
    public void deleteItem(Long id) {
        restClient.delete()
                .uri("/api/lost-found/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
