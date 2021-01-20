package com.marekhudyma.style.util;


import lombok.extern.log4j.Log4j2;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("db_url", postgreSQLContainer::getJdbcUrl);
        registry.add("db_username", postgreSQLContainer::getUsername);
        registry.add("db_password", postgreSQLContainer::getPassword);
    }

}
