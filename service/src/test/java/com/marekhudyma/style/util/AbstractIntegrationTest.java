package com.marekhudyma.style.util;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:10.4")
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

    @Autowired
    protected ConfigurableApplicationContext configurableApplicationContext;

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
