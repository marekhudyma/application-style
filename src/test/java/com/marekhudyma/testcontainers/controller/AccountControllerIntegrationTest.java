package com.marekhudyma.testcontainers.controller;

import com.marekhudyma.testcontainers.controller.dto.AccountDto;
import com.marekhudyma.testcontainers.controller.dto.AccountDtoTestBuilder;
import com.marekhudyma.testcontainers.model.Account;
import com.marekhudyma.testcontainers.model.AccountTestBuilder;
import com.marekhudyma.testcontainers.queue.MessageDto;
import com.marekhudyma.testcontainers.queue.TestQueueReceiver;
import com.marekhudyma.testcontainers.repository.AccountRepository;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.model.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.util.Optional;

import static com.marekhudyma.testcontainers.util.Resources.readFromResources;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.TestCase.assertEquals;
import static org.awaitility.Awaitility.await;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpMethod.POST;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;


public class AccountControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    private TestQueueReceiver testQueueReceiver;

    @Before
    public void setUp() throws Exception {
        testQueueReceiver.clean();
        mockServerContainer.getClient().reset();
    }

    @After
    public void tearDown() throws Exception {
        testQueueReceiver.clean();
    }

    @Test
    public void shouldCreateAccount() {
        // given
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        mockServerContainer.getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(200)
                        .withBody(readFromResources("additionalInfo1.json")));
        AccountDto accountDto = new AccountDtoTestBuilder(1).withTestDefaults().build();

        // when
        ResponseEntity<AccountDto> responseEntity = restTemplate.exchange("/accounts",
                POST,
                new HttpEntity<>(accountDto),
                AccountDto.class);

        // than
        mockServerContainer.getClient().verify(request);
        assertEquals("should be equal", 201, responseEntity.getStatusCodeValue());

        Account actual = accountRepository.findByName("name.1").get();
        Account expected = new AccountTestBuilder(1).withTestDefaults().created(null).name("name.1").build();
        assertReflectionEquals(expected, actual, ReflectionComparatorMode.IGNORE_DEFAULTS);

        await().atMost(120, SECONDS)
                .pollInterval(100, MILLISECONDS)
                .until(() -> {
                    Optional<MessageDto> messageOptional = testQueueReceiver.getMessages().stream()
                            .filter(a -> a.getId().equals(expected.getId()))
                            .findFirst();

                    return messageOptional.isPresent();
                });
    }
}
