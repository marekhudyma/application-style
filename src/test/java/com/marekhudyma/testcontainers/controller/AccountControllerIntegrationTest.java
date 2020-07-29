package com.marekhudyma.testcontainers.controller;

import com.marekhudyma.testcontainers.domain.model.Account;
import com.marekhudyma.testcontainers.persistence.AccountRepository;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


class AccountControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected AccountRepository accountRepository;


    @BeforeEach
    void setUp() throws Exception {
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
    void shouldCreateAccount() {
//TODO HUDYMA
//        // given
//        HttpRequest request = request("/api/entity/name.1").withMethod("GET");
//        getMockServerContainer().getClient().when(request)
//                .respond(response()
//                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
//                        .withStatusCode(200)
//                        .withBody(readFromResources("additionalInfo1.json")));
//        AccountDto accountDto = new AccountDtoTestBuilder(1).withTestDefaults().build();
//
//        // when
//        ResponseEntity<AccountDto> responseEntity = restTemplate.exchange("/accounts",
//                POST,
//                new HttpEntity<>(accountDto),
//                AccountDto.class);
//
//        // than
//        getMockServerContainer().getClient().verify(request);
//        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
//
//        Account actual = accountRepository.findByName("name.1").get();
//        Account expected = new AccountTestBuilder(1).withTestDefaults().created(null).name("name.1").build();
//        assertThat(actual).isEqualToIgnoringGivenFields(expected, "id", "created");


    }
}
