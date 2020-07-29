package com.marekhudyma.testcontainers.http.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Data
public class GetAccountDto {

    private final long id;

    private final String name;

    private final int scoring;

}
