package com.marekhudyma.testcontainers.client;

import com.marekhudyma.testcontainers.client.dto.ExternalServiceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExternalServiceClient {

    private final RestTemplate restTemplate;

    @Value("${externalService.url}")
    private String externalServiceUrl;

    public Optional<ExternalServiceResponseDto> getExternal(String name) {
        try {
            ResponseEntity<ExternalServiceResponseDto> response = restTemplate.getForEntity("{externalServiceUrl}/api/entity/{name}",
                    ExternalServiceResponseDto.class,
                    externalServiceUrl,
                    name);

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            } else {
                return Optional.empty();
            }
        } catch (RestClientException exception) {
            return Optional.empty();
        }

    }
}
