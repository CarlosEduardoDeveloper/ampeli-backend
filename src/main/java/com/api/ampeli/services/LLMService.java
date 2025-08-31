package com.api.ampeli.services;

import com.api.ampeli.model.dto.ConnectionRequest;
import com.api.ampeli.model.dto.ConnectionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LLMService {

    @Value("${llm.service.url:http://localhost:8001}")
    private String llmServiceUrl;

    private final RestTemplate restTemplate;

    public LLMService() {
        this.restTemplate = new RestTemplate();
    }

    public ConnectionResponse getRecommendations(ConnectionRequest request) {
        try {
            String url = llmServiceUrl + "/recommendations";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ConnectionRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ConnectionResponse> response = restTemplate.postForEntity(
                url, entity, ConnectionResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("LLM service returned error: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with LLM service: " + e.getMessage(), e);
        }
    }

    public boolean isLLMServiceAvailable() {
        try {
            String url = llmServiceUrl + "/health";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
