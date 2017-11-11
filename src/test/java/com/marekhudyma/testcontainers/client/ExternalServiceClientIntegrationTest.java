package com.marekhudyma.testcontainers.client;

import com.marekhudyma.testcontainers.client.dto.ExternalServiceResponseDto;
import com.marekhudyma.testcontainers.client.dto.ExternalServiceResponseDtoTestBuilder;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.model.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.marekhudyma.testcontainers.util.Resources.readFromResources;
import static junit.framework.TestCase.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ExternalServiceClientIntegrationTest extends AbstractIntegrationTest {

    private static final int DELTA_IN_MS = 100;

    @Autowired
    private ExternalServiceClient externalServiceClient;

    @Value("${timeoutInMs}")
    private int timeoutInMs;

    @Before
    public void setUp() throws Exception {
        mockServerContainer.getClient().reset();
    }

    @Test
    public void shouldGetResponseFromExternalService() throws Exception {
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        mockServerContainer.getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(200)
                        .withBody(readFromResources("additionalInfo1.json")));
        ExternalServiceResponseDto expected = new ExternalServiceResponseDtoTestBuilder(1)
                .withTestDefaults().build();

        Optional<ExternalServiceResponseDto> actual = externalServiceClient.getExternal("name.1");

        assertEquals("should be equal", Optional.of(expected), actual);
        mockServerContainer.getClient().verify(request);
    }

    @Test
    public void shouldFailBecauseOfTimeoutFromExternalService() throws Exception {
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        mockServerContainer.getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(200)
                        .withDelay(TimeUnit.MILLISECONDS, timeoutInMs + DELTA_IN_MS)
                        .withBody(readFromResources("additionalInfo1.json")));
        Optional<ExternalServiceResponseDto> expected = Optional.empty();

        Optional<ExternalServiceResponseDto> actual = externalServiceClient.getExternal("name.1");

        assertEquals("should be empty", expected, actual);
        mockServerContainer.getClient().verify(request);
    }

    @Test
    public void shouldFailBecauseOf404ResponseFromExternalService() throws Exception {
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        mockServerContainer.getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(404));
        Optional<ExternalServiceResponseDto> expected = Optional.empty();

        Optional<ExternalServiceResponseDto> actual = externalServiceClient.getExternal("name.1");

        assertEquals("should be empty", expected, actual);
        mockServerContainer.getClient().verify(request);
    }

    @Test
    public void shouldFailBecauseOfEmptyContentFromExternalService() throws Exception {
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        mockServerContainer.getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(200)
                        .withBody(""));
        Optional<ExternalServiceResponseDto> expected = Optional.empty();

        Optional<ExternalServiceResponseDto> actual = externalServiceClient.getExternal("name.1");

        assertEquals("should be empty", expected, actual);
        mockServerContainer.getClient().verify(request);
    }

}
