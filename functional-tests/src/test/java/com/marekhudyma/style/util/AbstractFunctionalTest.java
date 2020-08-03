package com.marekhudyma.style.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.nio.file.Paths;
import java.time.Duration;

import static java.util.Optional.ofNullable;

public class AbstractFunctionalTest {

    private static final int HTTP_PORT = 8080;

    private static final int DEBUG_PORT = 5005;

    private static final Logger LOGGER = LoggerFactory.getLogger("Docker-Container");

    private static final Network network = Network.newNetwork();

    public static final PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:12.4")
            .withUsername("marek")
            .withPassword("password")
            .withDatabaseName("application-style")
            .withNetwork(network)
            .withNetworkAliases("postgres");

    // If no version is set then use localhost:8080 and try to build the ccp container with jar artifact on target
    private static final GenericContainer<?> backendContainer;

    static {
        postgreSQLContainer.start();
        backendContainer = ofNullable(System.getenv("CONTAINER_VERSION"))
                .map(
                        version -> new ServiceContainer("docker-repository/application-style", version)
                ).orElseGet(
                        () -> new ServiceContainer(".", Paths.get("../"))
                )
                .withExposedPorts(HTTP_PORT, DEBUG_PORT)
                .withFixedExposedPort(DEBUG_PORT, DEBUG_PORT)
                .withEnv("SPRING_PROFILES_ACTIVE", "functional")
                .withEnv("ADDITIONAL_JAVA_OPTIONS", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:" + DEBUG_PORT)
                .withNetwork(network)
                .withCreateContainerCmdModifier(cmd -> cmd.withName("application-style"))
                .withLogConsumer(new Slf4jLogConsumer(LOGGER).withPrefix("Service"))
                .waitingFor(Wait.forHttp("/actuator/health").forPort(HTTP_PORT).withStartupTimeout(Duration.ofMinutes(2)));

        backendContainer.start();

        // make sure that containers will be stop in fast way (Ryuk can be slow)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("DockerContainers stop");
            backendContainer.stop();
            postgreSQLContainer.stop();
        }));
    }

    protected static RestTemplate getTestRestTemplate() {
        var endpoint = backendContainer.getContainerIpAddress();
        var port = backendContainer.getMappedPort(HTTP_PORT);
        var restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setUriTemplateHandler(new RootUriTemplateHandler(String.format("http://%s:%s", endpoint, port)));
        return restTemplate;
    }
}