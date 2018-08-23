package com.marekhudyma.testcontainers.util;

import com.google.common.collect.ImmutableList;
import com.marekhudyma.testcontainers.queue.TestQueueReceiver;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = com.marekhudyma.testcontainers.util.AbstractIntegrationTest.Initializer.class)
//We also need to refresh the ApplicationContext, because new docker is spawned with new ports
@DirtiesContext
public abstract class AbstractIntegrationTest {

    protected static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:10.4")
            .withUsername("marek")
            .withPassword("password")
            .withDatabaseName("experimentDB");

    protected static MockServerContainer mockServerContainer = new MockServerContainer("5.4.1");

    protected static GenericContainer rabbitMqContainer = new GenericContainer("rabbitmq:3.7.7") //3.6.16
            .withExposedPorts(5672);

    @Autowired
    protected TestQueueReceiver testQueueReceiver;

    @BeforeAll
    static void abstractBeforeAll() {
        postgreSQLContainer.start();
        rabbitMqContainer.start();
        mockServerContainer.start();
    }

    @AfterAll
    static void abstractAfterAll() {
        RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry = StaticContextAccessor.getBean(RabbitListenerEndpointRegistry.class);
        rabbitListenerEndpointRegistry.getListenerContainers().forEach(Lifecycle::stop);

        postgreSQLContainer.stop();
        rabbitMqContainer.stop();
        mockServerContainer.stop();
    }

    @BeforeEach
    void abstractBeforeEach() {
        if (mockServerContainer.getClient() != null) {
            mockServerContainer.getClient().reset();
        }
        testQueueReceiver.clean();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(new ImmutableList.Builder<String>()
                    .add("db_url=" + postgreSQLContainer.getJdbcUrl())
                    .add("externalService.url=" + mockServerContainer.getEndpoint())
                    .add("spring.rabbitmq.host=" + rabbitMqContainer.getContainerIpAddress())
                    .add("spring.rabbitmq.port=" + rabbitMqContainer.getMappedPort(5672).toString())
                    .build()).applyTo(configurableApplicationContext);
            ConfigurationPropertySources.attach(configurableApplicationContext.getEnvironment());
        }
    }
}
