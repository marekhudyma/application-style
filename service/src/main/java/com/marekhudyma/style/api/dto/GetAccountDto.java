package com.marekhudyma.style.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record GetAccountDto(

        @JsonProperty("id")
        UUID id,

        @JsonProperty("name")
        String name,

        @JsonProperty("scoring")
        int scoring

) {

}


