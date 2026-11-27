
package com.surakshasetu.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class StudentComplaintService {

    private final RestClient complaintsRestClient;

    public StudentComplaintService(
        @Qualifier("complaintsRestClient") RestClient complaintsRestClient) {
        this.complaintsRestClient = complaintsRestClient;
    }

    public List<Map<String, Object>> getStudentComplaints(Long studentId) {

        return complaintsRestClient.get()
                .uri("/api/complaints/student/{studentId}", studentId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    public Map<String, Object> getComplaintById(Long complaintId) {
        return complaintsRestClient.get()
                .uri("/api/complaints/{id}", complaintId)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public List<Map<String, Object>> getComplaintHistory(Long complaintId) {

        return complaintsRestClient.get()
                .uri("/api/complaints/{id}/history", complaintId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }

    public Map<String, Object> createComplaint(
        Map<String, Object> complaintData) {

        return complaintsRestClient.post()
                .uri("/api/complaints")
                .contentType(MediaType.APPLICATION_JSON)
                .body(complaintData)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}
