package com.marekhudyma.testcontainers.domain.service;

import com.marekhudyma.testcontainers.domain.exception.AccountExistException;
import com.marekhudyma.testcontainers.domain.model.Account;
import com.marekhudyma.testcontainers.domain.model.AccountTestBuilder;
import com.marekhudyma.testcontainers.persistence.AccountRepository;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Log4j2
class AccountServiceIntegrationTest extends AbstractIntegrationTest {

    private static final int DELTA_IN_MS = 100;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService unterTest;

    @Value("${timeoutInMs}")
    private int timeoutInMs;

    private Account account;

    @BeforeEach
    void setUp() throws Exception {
        account = new AccountTestBuilder(1).withTestDefaults().id(null).name("name.1").build();
        clean();
    }

    @AfterEach
    void tearDown() throws Exception {
        clean();
    }

    void clean() {
        Optional<Account> accountOtional = accountRepository.findByName("name.1");
        accountOtional.ifPresent(account1 -> accountRepository.delete(account1));
    }

    @Test
    void shouldSaveAccountToDatabase() {
//TODO HUDYMA
//        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
//        getMockServerContainer().getClient().when(request)
//                .respond(response()
//                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
//                        .withStatusCode(200)
//                        .withBody(readFromResources("additionalInfo1.json")));
//
//        Account actual = unterTest.createAccount(account);
//
//        assertThat(actual).isEqualTo(account);
//        getMockServerContainer().getClient().verify(request);
    }

    @Test
    void shouldFailBecauseOfEmptyContentFromExternalService() throws Exception {
//TODO HUDYMA
//        accountRepository.save(account);
//
//        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
//        getMockServerContainer().getClient().when(request)
//                .respond(response()
//                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
//                        .withStatusCode(200)
//                        .withBody(""));
//
//        assertThrows(AccountExistException.class, () -> unterTest.createAccount(account));
    }

}
