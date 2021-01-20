package com.marekhudyma.style.util;


import lombok.extern.log4j.Log4j2;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.validation.constraints.NotNull;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:13.1")
            .withUsername("marek")
            .withPassword("password")
            .withDatabaseName("application-style");

    static {
        postgreSQLContainer.start();
        // make sure that containers will be stop in fast way (Ryuk can be slow)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("DockerContainers stop");
            postgreSQLContainer.stop();
        }));
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "db_url=" + postgreSQLContainer.getJdbcUrl(),
                    "db_username=" + postgreSQLContainer.getUsername(),
                    "db_password=" + postgreSQLContainer.getPassword()
            );
            values.applyTo(configurableApplicationContext);
        }
    }
}
