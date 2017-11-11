package com.marekhudyma.testcontainers.util;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
//We also need to refresh the ApplicationContext, because new docker is spawned with new ports
@DirtiesContext
public abstract class AbstractIntegrationTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:10.4")
            .withUsername("marek")
            .withPassword("password")
            .withDatabaseName("experimentDB");

    @ClassRule
    public static MockServerContainer mockServerContainer = new MockServerContainer("5.4.1");

    @ClassRule
    public static GenericContainer rabbitMqContainer = new GenericContainer("rabbitmq:3.7.7") //3.6.16
            .withExposedPorts(5672);

    @Before
    public void setUp() throws Exception {
        mockServerContainer.getClient().reset();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            EnvironmentTestUtils.addEnvironment(configurableApplicationContext.getEnvironment(),
                    "db_url=" + postgreSQLContainer.getJdbcUrl(),
                    "externalService.url=" + mockServerContainer.getEndpoint(),
                    "spring.rabbitmq.host=" + rabbitMqContainer.getContainerIpAddress(),
                    "spring.rabbitmq.port=" + rabbitMqContainer.getMappedPort(5672)
            );
        }
    }
}
