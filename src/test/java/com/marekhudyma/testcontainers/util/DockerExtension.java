package com.marekhudyma.testcontainers.util;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Log4j2
public class DockerExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
        DockerContainers.startIfNotStarted();
    }

}