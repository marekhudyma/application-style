package com.marekhudyma.testcontainers.client.dto;

import lombok.RequiredArgsConstructor;

import static java.lang.String.format;

@RequiredArgsConstructor
public class ExternalServiceResponseDtoTestBuilder extends ExternalServiceResponseDto.ExternalServiceResponseDtoBuilder {

    private final int seed;

    public ExternalServiceResponseDto.ExternalServiceResponseDtoBuilder withTestDefaults() {
        return ExternalServiceResponseDto.builder()
                .additionalInfo(format("additionalInfo.%d", seed));
    }
}
