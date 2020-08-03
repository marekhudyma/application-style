package com.marekhudyma.style.api.controller;

import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.model.AccountId;
import com.marekhudyma.style.domain.model.AccountTestBuilder;
import com.marekhudyma.style.persistence.AccountRepository;
import com.marekhudyma.style.util.AbstractIntegrationTest;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.marekhudyma.style.util.TestResources.readFromResources;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

class AccountControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected AccountRepository accountRepository;

    @AfterEach
    void tearDown() {
        accountRepository.findById(AccountId.from(1)).ifPresent(a -> accountRepository.deleteById(AccountId.from(1)));
    }

    private String getEtagForAccountId(AccountId id) {
        ResponseEntity<String> response = restTemplate.getForEntity(format("/accounts/%s", id.getValue()), String.class);
        return response.getHeaders().getETag();
    }

    private Account saveAccount(int id) {
        return accountRepository.saveAndFlush(new AccountTestBuilder(1).withTestDefaults()
                .id(AccountId.from(id))
                .version(null)
                .build());
    }

    private HttpHeaders getPostHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private HttpHeaders getPatchHeaders(String etag) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "merge-patch+json"));
        headers.add(HttpHeaders.ETAG, etag);
        return headers;
    }

    @Nested
    @DisplayName("POST /accounts")
    class CreateAccount {

        @Test
        void shouldCreateAccount() throws JSONException {
            // given
            var json = readFromResources("http/controller/account_controller_integration_test/create_account_dto.json");

            // when
            ResponseEntity<String> response = restTemplate.exchange("/accounts",
                    HttpMethod.POST,
                    new HttpEntity<>(json, getPostHeaders()),
                    String.class);

            // then
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
            var expected = readFromResources("http/controller/account_controller_integration_test/get_account_dto.json");
            JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);

            Optional<Account> accountOptional = accountRepository.findById(AccountId.from(1));
            assertThat(accountOptional).isPresent();
            var accountExpected = new AccountTestBuilder(1).withTestDefaults().version(0).build();
            assertThat(accountOptional.get()).isEqualTo(accountExpected);
        }

        @Test
        void shouldNotCreateAccount() throws JSONException {
            // given
            saveAccount(1);
            var json = readFromResources("http/controller/account_controller_integration_test/create_account_dto.json");

            // when
            ResponseEntity<String> response = restTemplate.exchange("/accounts",
                    HttpMethod.POST,
                    new HttpEntity<>(json, getPostHeaders()),
                    String.class);

            // then
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CONFLICT.value());
            var expected = readFromResources("http/controller/account_controller_integration_test/get_account_dto.json");
            JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);

            Optional<Account> accountOptional = accountRepository.findById(AccountId.from(1));
            assertThat(accountOptional).isPresent();
            var accountExpected = new AccountTestBuilder(1).withTestDefaults().version(0).build();
            assertThat(accountOptional.get()).isEqualTo(accountExpected);
        }

    }

    @Nested
    @DisplayName("GET /accounts/{id}")
    class GetAccount {

        @Test
        void shouldGetAccount() throws JSONException {
            // given
            saveAccount(1);

            // when
            ResponseEntity<String> response = restTemplate.getForEntity("/accounts/00000000-0000-0000-0000-000000000001", String.class);

            // then
            String etag = response.getHeaders().getETag();
            assertThat(etag).isNotEmpty();
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
            var expected = readFromResources("http/controller/account_controller_integration_test/get_account_dto.json");
            JSONAssert.assertEquals(expected, response.getBody(), JSONCompareMode.LENIENT);
        }

        @Test
        void shouldNotGetAccountBecauseNotExist() {
            // given
            saveAccount(1);

            // when
            ResponseEntity<String> response = restTemplate.getForEntity("/accounts/00000000-0000-0000-0000-999999999999", String.class);

            // then
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

    }

    @Nested
    @DisplayName("PATCH /accounts/{id}")
    class PatchAccount {

        @Test
        void shouldPatchAccount() {
            // given
            var json = readFromResources("http/controller/account_controller_integration_test/patch_account_dto.json");
            saveAccount(1);
            String etag = getEtagForAccountId(AccountId.from(1));

            // when
            ResponseEntity<String> response = restTemplate.exchange("/accounts/00000000-0000-0000-0000-000000000001",
                    HttpMethod.PATCH,
                    new HttpEntity<>(json, getPatchHeaders(etag)),
                    String.class);

            // then
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());

            Optional<Account> accountOptional = accountRepository.findById(AccountId.from(1));
            assertThat(accountOptional).isPresent();
            var accountExpected = new AccountTestBuilder(1).withTestDefaults().scoring(2).version(1).build();
            assertThat(accountOptional.get()).isEqualTo(accountExpected);
        }

        @Test
        void shouldNotPatchAccountBecauseNotExist() {
            // given
            var json = readFromResources("http/controller/account_controller_integration_test/patch_account_dto.json");
            String etag = "9999999";

            // when
            ResponseEntity<String> response = restTemplate.exchange("/accounts/00000000-0000-0000-0000-999999999999",
                    HttpMethod.PATCH,
                    new HttpEntity<>(json, getPatchHeaders(etag)),
                    String.class);

            // then
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.NOT_FOUND.value());

            Optional<Account> accountOptional = accountRepository.findById(AccountId.from(1));
            assertThat(accountOptional).isEmpty();
        }

    }

}
