package com.marekhudyma.style.domain.model;

import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static java.lang.String.format;


@RequiredArgsConstructor
public class AccountTestBuilder extends Account.AccountBuilder {

    private final int seed;

    public Account.AccountBuilder withTestDefaults() {
        return Account.builder()
                .id(AccountId.from(seed))
                .createdAt(Instant.ofEpochMilli(seed))
                .updatedAt(Instant.ofEpochMilli(seed))
                .name(format("name.%d", seed))
                .scoring(seed)
                .version(seed);
    }

}