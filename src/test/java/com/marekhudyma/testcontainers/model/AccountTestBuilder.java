package com.marekhudyma.testcontainers.model;

import lombok.RequiredArgsConstructor;

import java.time.Instant;

import static java.lang.String.format;


@RequiredArgsConstructor
public class AccountTestBuilder extends Account.AccountBuilder {

    private final int seed;

    public Account.AccountBuilder withTestDefaults() {
        return Account.builder()
                .id((long) seed)
                .created(Instant.ofEpochMilli(seed))
                .name(format("name.%d", seed))
                .additionalInfo(format("additionalInfo.%d", seed))
                .scoring(seed);
    }

}


