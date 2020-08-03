package com.marekhudyma.style.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

//@Builder
//@RequiredArgsConstructor
//@Data
//public class CreateAccountDto {
//
//    @NotNull
//    private final UUID id;
//
//    @NotBlank
//    @Max(50)
//    private final String name;
//
//    @Min(0)
//    @Max(100)
//    private final int scoring;
//
//}

// TODO remove @JsonProperty when Jackson support records
public record CreateAccountDto(

        @NotNull
        @JsonProperty("id")
        UUID id,

        @NotEmpty
        @JsonProperty("name")
        String name,

        @NotNull
        @Min(0)
        @Max(100)
        @JsonProperty("scoring")
        Integer scoring

) {

}

