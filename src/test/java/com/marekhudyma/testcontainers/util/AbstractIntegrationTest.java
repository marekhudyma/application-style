package com.marekhudyma.testcontainers.util;

import com.google.common.collect.ImmutableList;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected ConfigurableApplicationContext configurableApplicationContext;

    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:10.4")
            .withUsername("marek")
            .withPassword("password")
            .withDatabaseName("experimentDB");

    private static boolean started = false;

    static {
        postgreSQLContainer.start();
        // make sure that containers will be stop
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("DockerContainers stop");
            postgreSQLContainer.stop();
        }));
    }

    public PostgreSQLContainer getPostgreSQLContainer() {
        return postgreSQLContainer;
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(new ImmutableList.Builder<String>()
                    .add("db_url=" + postgreSQLContainer.getJdbcUrl())
                    .build()).applyTo(configurableApplicationContext);
            ConfigurationPropertySources.attach(configurableApplicationContext.getEnvironment());
        }
    }

}
