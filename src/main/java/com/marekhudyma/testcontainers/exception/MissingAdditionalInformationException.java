package com.marekhudyma.testcontainers.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MissingAdditionalInformationException extends RuntimeException {

    private final String name;

}
