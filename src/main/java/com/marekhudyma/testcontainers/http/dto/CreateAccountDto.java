package com.marekhudyma.testcontainers.http.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.*;

@Builder
@RequiredArgsConstructor
@Data
public class CreateAccountDto {

    @NotBlank
    @Max(50)
    private final String name;

    @Min(0)
    @Max(100)
    private final int scoring;

}
