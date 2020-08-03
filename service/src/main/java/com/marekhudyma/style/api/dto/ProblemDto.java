package com.marekhudyma.style.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProblemDto(

        @JsonProperty("title")
        String title,

        @JsonProperty("statusCode")
        int statusCode,

        @JsonProperty("detail")
        String detail
) {

}
