package com.marekhudyma.testcontainers.controller.dto;

import lombok.RequiredArgsConstructor;

import static java.lang.String.format;


@RequiredArgsConstructor
public class AccountDtoTestBuilder extends AccountDto.AccountDtoBuilder {

    private final int seed;

    public AccountDto.AccountDtoBuilder withTestDefaults() {
        return AccountDto.builder()
                .name(format("name.%d", seed))
                .scoring(seed);
    }

}


