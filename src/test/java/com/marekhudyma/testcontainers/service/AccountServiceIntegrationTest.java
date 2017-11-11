package com.marekhudyma.testcontainers.service;

import com.marekhudyma.testcontainers.exception.AccountExistException;
import com.marekhudyma.testcontainers.exception.MissingAdditionalInformationException;
import com.marekhudyma.testcontainers.model.Account;
import com.marekhudyma.testcontainers.model.AccountTestBuilder;
import com.marekhudyma.testcontainers.repository.AccountRepository;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.After;
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

public class AccountServiceIntegrationTest extends AbstractIntegrationTest {

    private static final int DELTA_IN_MS = 100;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService unterTest;

    @Value("${timeoutInMs}")
    private int timeoutInMs;

    private Account account;

    @Before
    public void setUp() throws Exception {
        mockServerContainer.getClient().reset();
        account = new AccountTestBuilder(1).withTestDefaults().id(null).name("name.1").build();
    }

    @After
    public void tearDown() throws Exception {
        Optional<Account> accountOtional = accountRepository.findByName("name.1");
        accountOtional.ifPresent(account1 -> accountRepository.delete(account1));
    }

    @Test
    public void shouldSaveAccountToDatabase() {
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        mockServerContainer.getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(200)
                        .withBody(readFromResources("additionalInfo1.json")));

        Account actual = unterTest.createAccount(account);

        assertEquals("should be equal", account, actual);
        mockServerContainer.getClient().verify(request);
    }

    @Test(expected = MissingAdditionalInformationException.class)
    public void shouldNotSaveAccountWhenProblemWithCommunicationWithExternalService() {
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        mockServerContainer.getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(200)
                        .withDelay(TimeUnit.MILLISECONDS, timeoutInMs + DELTA_IN_MS)
                        .withBody(readFromResources("additionalInfo1.json")));

        unterTest.createAccount(account);
    }

    @Test(expected = MissingAdditionalInformationException.class)
    public void shouldFailBecauseOf404ResponseFromExternalService() {
        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        mockServerContainer.getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(404));

        unterTest.createAccount(account);
    }

    @Test(expected = AccountExistException.class)
    public void shouldFailBecauseOfEmptyContentFromExternalService() throws Exception {
        accountRepository.save(account);

        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
        mockServerContainer.getClient().when(request)
                .respond(response()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                        .withStatusCode(200)
                        .withBody(""));

        unterTest.createAccount(account);
    }

}
