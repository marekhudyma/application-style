package com.marekhudyma.testcontainers.util;

import lombok.extern.log4j.Log4j2;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@Log4j2
public class DockerContainers {

    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:10.4")
            .withUsername("marek")
            .withPassword("password")
            .withDatabaseName("experimentDB");
    private static MockServerContainer mockServerContainer = new MockServerContainer("5.4.1");
    private static GenericContainer rabbitMqContainer = new GenericContainer("rabbitmq:3.7.7")
            .withExposedPorts(5672);
    private static boolean started = false;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("DockerContainers stop");
                postgreSQLContainer.stop();
                mockServerContainer.stop();
                rabbitMqContainer.stop();
            }
        }));
    }

    public static void startIfNotStarted() {
        if (!started) {
            log.info("DockerContainers start");
            DockerContainers.getPostgreSQLContainer().start();
            DockerContainers.getRabbitMqContainer().start();
            DockerContainers.getMockServerContainer().start();
            started = true;
        }
    }

    public static PostgreSQLContainer getPostgreSQLContainer() {
        return postgreSQLContainer;
    }

    public static MockServerContainer getMockServerContainer() {
        return mockServerContainer;
    }

    public static GenericContainer getRabbitMqContainer() {
        return rabbitMqContainer;
    }


}
