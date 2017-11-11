package com.marekhudyma.testcontainers.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountExistException extends RuntimeException {

    private final String name;

}
