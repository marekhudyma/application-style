package com.marekhudyma.testcontainers.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

public class Resources {

    public static String readFromResources(String file) {
        try {
            return new String(Files.readAllBytes(new ClassPathResource("json/" + file).getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
