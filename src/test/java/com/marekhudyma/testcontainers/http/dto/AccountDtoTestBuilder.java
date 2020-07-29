package com.marekhudyma.testcontainers.http.dto;

import lombok.RequiredArgsConstructor;

import static java.lang.String.format;


@RequiredArgsConstructor
public class AccountDtoTestBuilder extends CreateAccountDto.CreateAccountDtoBuilder {

    private final int seed;

    public CreateAccountDto.CreateAccountDtoBuilder withTestDefaults() {
        return CreateAccountDto.builder()
                .name(format("name.%d", seed))
                .scoring(seed);
    }

}


