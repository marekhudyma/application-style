package com.marekhudyma.testcontainers.client;

import com.marekhudyma.testcontainers.client.dto.ExternalServiceResponseDto;
import com.marekhudyma.testcontainers.client.dto.ExternalServiceResponseDtoTestBuilder;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.marekhudyma.testcontainers.util.Resources.readFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class ExternalServiceClientIntegrationTest extends AbstractIntegrationTest {

    private static final int DELTA_IN_MS = 100;

    @Autowired
    private ExternalServiceClient externalServiceClient;

    @Value("${timeoutInMs}")
    private int timeoutInMs;

    @Test
    void shouldGetResponseFromExternalService() throws Exception {
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        getMockServerContainer().getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(200)
                        .withBody(readFromResources("additionalInfo1.json")));
        ExternalServiceResponseDto expected = new ExternalServiceResponseDtoTestBuilder(1)
                .withTestDefaults().build();

        Optional<ExternalServiceResponseDto> actual = externalServiceClient.getExternal("name.1");

        assertThat(actual.get()).isEqualTo(expected);
        getMockServerContainer().getClient().verify(request);
    }

    @Test
    void shouldFailBecauseOfTimeoutFromExternalService() throws Exception {
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        getMockServerContainer().getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(200)
                        .withDelay(TimeUnit.MILLISECONDS, timeoutInMs + DELTA_IN_MS)
                        .withBody(readFromResources("additionalInfo1.json")));
        Optional<ExternalServiceResponseDto> expected = Optional.empty();

        Optional<ExternalServiceResponseDto> actual = externalServiceClient.getExternal("name.1");

        assertThat(actual).isEqualTo(expected);
        getMockServerContainer().getClient().verify(request);
    }

    @Test
    void shouldFailBecauseOf404ResponseFromExternalService() throws Exception {
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        getMockServerContainer().getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(404));
        Optional<ExternalServiceResponseDto> actual = externalServiceClient.getExternal("name.1");

        assertThat(actual).isEmpty();
        getMockServerContainer().getClient().verify(request);
    }

    @Test
    void shouldFailBecauseOfEmptyContentFromExternalService() throws Exception {
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        getMockServerContainer().getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(200)
                        .withBody(""));
        Optional<ExternalServiceResponseDto> actual = externalServiceClient.getExternal("name.1");

        assertThat(actual).isEmpty();
        getMockServerContainer().getClient().verify(request);
    }

}
