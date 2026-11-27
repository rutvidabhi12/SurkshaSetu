package com.surakshasetu.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // Complaints Service - Port 8083
    @Bean
    public RestClient complaintsRestClient(
            @Value("${complaints.service.url}") String baseUrl) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

     // Lost & Found Service - Port 8085
    @Bean
    public RestClient lostFoundRestClient(
            @Value("${lostfound.service.url}") String baseUrl) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
