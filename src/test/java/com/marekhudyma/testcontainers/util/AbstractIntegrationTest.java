package com.marekhudyma.testcontainers.util;

import com.google.common.collect.ImmutableList;
import com.marekhudyma.testcontainers.queue.TestQueueReceiver;
import org.jetbrains.annotations.NotNull;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(DockerExtension.class)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected TestQueueReceiver testQueueReceiver;

    @Autowired
    protected ConfigurableApplicationContext configurableApplicationContext;

    @BeforeEach
    void abstractBeforeEach() {
        if (DockerContainers.getMockServerContainer().getClient() != null) {
            DockerContainers.getMockServerContainer().getClient().reset();
        }
        testQueueReceiver.clean();
    }

    public PostgreSQLContainer getPostgreSQLContainer() {
        return DockerContainers.getPostgreSQLContainer();
    }

    public MockServerContainer getMockServerContainer() {
        return DockerContainers.getMockServerContainer();
    }

    public GenericContainer getRabbitMqContainer() {
        return DockerContainers.getRabbitMqContainer();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(new ImmutableList.Builder<String>()
                    .add("db_url=" + DockerContainers.getPostgreSQLContainer().getJdbcUrl())
                    .add("externalService.url=" + DockerContainers.getMockServerContainer().getEndpoint())
                    .add("spring.rabbitmq.host=" + DockerContainers.getRabbitMqContainer().getContainerIpAddress())
                    .add("spring.rabbitmq.port=" + DockerContainers.getRabbitMqContainer().getMappedPort(5672).toString())
                    .build()).applyTo(configurableApplicationContext);
            ConfigurationPropertySources.attach(configurableApplicationContext.getEnvironment());
        }
    }


}
