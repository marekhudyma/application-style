package com.marekhudyma.testcontainers.util;

import com.github.dockerjava.api.command.InspectContainerResponse;
import org.mockserver.client.MockServerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;

public class MockServerContainer extends GenericContainer<MockServerContainer> {
    public static final String VERSION = "5.3.0";
    public static final int PORT = 80;
    private MockServerClient client;

    public MockServerContainer() {
        this("5.3.0");
    }

    public MockServerContainer(String version) {
        super("jamesdbloom/mockserver:mockserver-" + version);
        this.withCommand("/opt/mockserver/run_mockserver.sh -logLevel INFO -serverPort 80");
        this.addExposedPorts(new int[]{80});
    }

    protected void containerIsStarted(InspectContainerResponse containerInfo) {
        super.containerIsStarted(containerInfo);
        this.client = new MockServerClient(this.getContainerIpAddress(), this.getMappedPort(80));
    }

    public String getEndpoint() {
        return String.format("http://%s:%d", this.getContainerIpAddress(), this.getMappedPort(80));
    }

    public MockServerClient getClient() {
        return this.client;
    }
}
