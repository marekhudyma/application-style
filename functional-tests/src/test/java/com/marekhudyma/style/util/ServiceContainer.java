package com.marekhudyma.style.util;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.InternetProtocol;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Path;


/**
 * Example of usage:
 * In you AbstractIntegrationTest define container:
 * backendContainer = ofNullable(System.getenv("CONTAINER_VERSION"))
 * .map(version -> new ServiceContainer("docker-repository/application-style", version))
 * .orElseGet(() -> new ServiceContainer(".", Paths.get("../")))
 * .withExposedPorts(HTTP_PORT, DEBUG_PORT)
 * .withFixedExposedPort(DEBUG_PORT, DEBUG_PORT)
 * .withEnv("SPRING_PROFILES_ACTIVE", "functional")
 * .withEnv("ADDITIONAL_JAVA_OPTIONS", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=0.0.0.0:" + DEBUG_PORT)
 * .withNetwork(network)
 * .withCreateContainerCmdModifier(cmd -> cmd.withName("application-style"))
 * .withLogConsumer(new Slf4jLogConsumer(LOGGER).withPrefix("Service"))
 * .waitingFor(Wait.forHttp("/actuator/health").forPort(HTTP_PORT).withStartupTimeout(Duration.ofMinutes(2)));
 * <p>
 * set flag: suspend=y - it will wait for remote debugger to connect
 * Inside IntelliJ add project "Remote", define the same port (usually 5005), select use module classpath: your sources.
 */
public class ServiceContainer extends GenericContainer<ServiceContainer> {

    public ServiceContainer(String path, Path filePath) {
        super(new ImageFromDockerfile().withFileFromPath(path, filePath));
    }

    public ServiceContainer(String dockerImageName, String version) {
        super(dockerImageName + ":" + version);
    }

    public ServiceContainer withFixedExposedPort(int hostPort, int containerPort) {
        super.addFixedExposedPort(hostPort, containerPort, InternetProtocol.TCP);
        return this;
    }
}