package com.marekhudyma.style.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record PatchAccountDto(

        @NotNull
        @Min(0)
        @Max(100)
        @JsonProperty("scoring")
        Integer scoring

) {

}

