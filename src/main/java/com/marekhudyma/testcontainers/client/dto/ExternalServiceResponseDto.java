package com.marekhudyma.testcontainers.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ExternalServiceResponseDto {

    private String additionalInfo;

}
