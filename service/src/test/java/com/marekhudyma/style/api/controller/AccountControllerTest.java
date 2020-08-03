package com.marekhudyma.style.api.controller;

import com.marekhudyma.style.api.converter.AccountToGetAccountDtoConverter;
import com.marekhudyma.style.api.converter.CreateAccountDtoToAccountConverter;
import com.marekhudyma.style.api.converter.PatchToAccountUpdateConverter;
import com.marekhudyma.style.api.dto.CreateAccountDto;
import com.marekhudyma.style.api.dto.GetAccountDto;
import com.marekhudyma.style.domain.command.CreateAccountCommand;
import com.marekhudyma.style.domain.command.UpdateAccountCommand;
import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.model.AccountId;
import com.marekhudyma.style.domain.model.AccountTestBuilder;
import com.marekhudyma.style.domain.query.GetAccountQuery;
import com.marekhudyma.style.domain.util.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.marekhudyma.style.util.TestResources.readFromResources;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @MockBean
    private CreateAccountCommand createAccountCommand;

    @MockBean
    private GetAccountQuery getAccountQuery;

    @MockBean
    private UpdateAccountCommand updateAccountCommand;

    @MockBean
    private PatchToAccountUpdateConverter patchToAccountUpdateConverter;

    @MockBean
    private CreateAccountDtoToAccountConverter createAccountDtoToAccountConverter;

    @MockBean
    private AccountToGetAccountDtoConverter accountToGetAccountDtoConverter;

    @Autowired
    private MockMvc mvc;

    @AfterEach
    void tearDown() {
        reset(createAccountCommand, getAccountQuery, updateAccountCommand,
                patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
    }

    private UpdateAccountCommand.AccountUpdate getUpdate(int id, int scoring, String etag) {
        return new UpdateAccountCommand.AccountUpdate() {
            @Override
            public AccountId getId() {
                return AccountId.from(id);
            }

            @Override
            public int getScoring() {
                return scoring;
            }

            @Override
            public String getVersion() {
                return etag;
            }
        };

    }

    @Nested
    @DisplayName("POST /accounts")
    class CreateAccount {

        @Test
        void shouldCreateAccount() throws Exception {
            // given
            Account account = new AccountTestBuilder(1).withTestDefaults().build();
            CreateAccountDto accountDto = new CreateAccountDto(new UUID(0, 1), "name.1", 1);
            GetAccountDto getAccountDto = new GetAccountDto(new UUID(0, 1), "name.1", 1);

            when(createAccountDtoToAccountConverter.convert(accountDto)).thenReturn(account);
            when(createAccountCommand.execute(account)).thenReturn(Result.ok(account));
            when(accountToGetAccountDtoConverter.convert(account)).thenReturn(getAccountDto);

            // when
            mvc.perform(
                    post("/accounts")
                            .contentType(APPLICATION_JSON_VALUE)
                            .content(readFromResources("http/controller/account_controller_test/create_account_dto.json")))
                    .andExpect(status().is(HttpStatus.CREATED.value()))
                    .andExpect(content().json(readFromResources("http/controller/account_controller_test/get_account_dto.json")));

            verify(createAccountDtoToAccountConverter).convert(accountDto);
            verify(createAccountCommand).execute(account);
            verify(accountToGetAccountDtoConverter).convert(account);
            verifyNoMoreInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }

        @Test
        void shouldReturnConflict() throws Exception {
            // given
            Account account = new AccountTestBuilder(1).withTestDefaults().build();
            CreateAccountDto accountDto = new CreateAccountDto(new UUID(0, 1), "name.1", 1);
            GetAccountDto getAccountDto = new GetAccountDto(new UUID(0, 1), "name.1", 1);

            when(createAccountDtoToAccountConverter.convert(accountDto)).thenReturn(account);
            when(createAccountCommand.execute(account))
                    .thenReturn(Result.fail(account, CreateAccountCommand.Error.ACCOUNT_ALREADY_EXIST));
            when(accountToGetAccountDtoConverter.convert(account)).thenReturn(getAccountDto);

            // when
            mvc.perform(
                    post("/accounts")
                            .contentType(APPLICATION_JSON_VALUE)
                            .content(readFromResources("http/controller/account_controller_test/create_account_dto.json")))
                    .andExpect(status().is(HttpStatus.CONFLICT.value()))
                    .andExpect(content().json(readFromResources("http/controller/account_controller_test/get_account_dto.json")));

            // then
            verify(createAccountDtoToAccountConverter).convert(accountDto);
            verify(createAccountCommand).execute(account);
            verify(accountToGetAccountDtoConverter).convert(account);
            verifyNoMoreInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }

        @Test
        void shouldReturnBadRequestForMissingId() throws Exception {
            mvc.perform(
                    post("/accounts")
                            .contentType(APPLICATION_JSON_VALUE)
                            .content(readFromResources("http/controller/account_controller_test/create_account_dto_missing_id.json")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

            verifyNoInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }

        @Test
        void shouldReturnBadRequestForMissingName() throws Exception {
            mvc.perform(
                    post("/accounts")
                            .contentType(APPLICATION_JSON_VALUE)
                            .content(readFromResources("http/controller/account_controller_test/create_account_dto_missing_name.json")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

            verifyNoInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }

        @Test
        void shouldReturnBadRequestForMissingScoring() throws Exception {
            mvc.perform(
                    post("/accounts")
                            .contentType(APPLICATION_JSON_VALUE)
                            .content(readFromResources("http/controller/account_controller_test/create_account_dto_missing_scoring.json")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

            verifyNoInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }

        @Test
        void shouldReturnBadRequestForScoringTooBig() throws Exception {
            mvc.perform(
                    post("/accounts")
                            .contentType(APPLICATION_JSON_VALUE)
                            .content(readFromResources("http/controller/account_controller_test/create_account_dto_scoring_too_big.json")))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

            verifyNoInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }

    }

    @Nested
    @DisplayName("GET /accounts")
    class GetAccount {

        @Test
        void shouldGetAccount() throws Exception {
            // given
            AccountId accountId = AccountId.from(1);
            Account account = new AccountTestBuilder(1).withTestDefaults().build();
            GetAccountDto getAccountDto = new GetAccountDto(new UUID(0, 1), "name.1", 1);

            when(getAccountQuery.execute(accountId)).thenReturn(Result.ok(account));
            when(accountToGetAccountDtoConverter.convert(account)).thenReturn(getAccountDto);

            // when
            mvc.perform(
                    get("/accounts/00000000-0000-0000-0000-000000000001"))
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andExpect(content().json(readFromResources("http/controller/account_controller_test/get_account_dto.json")))
                    .andExpect(header().string(HttpHeaders.ETAG, "\"1\""));

            // then
            verify(getAccountQuery).execute(accountId);
            verify(accountToGetAccountDtoConverter).convert(account);
            verifyNoMoreInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }

        @Test
        void shouldNotGetAccount() throws Exception {
            // given
            AccountId accountId = AccountId.from(1);
            when(getAccountQuery.execute(accountId)).thenReturn(Result.fail(GetAccountQuery.Error.ACCOUNT_NOT_FOUND));

            // when
            mvc.perform(
                    get("/accounts/00000000-0000-0000-0000-000000000001"))
                    .andExpect(status().is(HttpStatus.NOT_FOUND.value()));

            // then
            verify(getAccountQuery).execute(accountId);
            verifyNoMoreInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }

        @Test
        void shouldNotGetAccountBecauseBadId() throws Exception {
            mvc.perform(
                    get("/accounts/1"))
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

            verifyNoInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }
    }

    @Nested
    @DisplayName("PATCH /accounts")
    class UpdateAccount {

        @Test
        void shouldUpdateAccount() throws Exception {
            // given
            Account accountUpdated = new AccountTestBuilder(1).withTestDefaults().scoring(2).build();
            var accountUpdate = getUpdate(1, 2, "1");

            when(patchToAccountUpdateConverter.convert(new UUID(0, 1), 2, "1"))
                    .thenReturn(accountUpdate);
            when(updateAccountCommand.execute(accountUpdate)).thenReturn(Result.ok(accountUpdated));

            // when
            mvc.perform(
                    patch("/accounts/00000000-0000-0000-0000-000000000001")
                            .contentType("application/merge-patch+json")
                            .content(readFromResources("http/controller/account_controller_test/update_account_dto.json"))
                            .header(HttpHeaders.ETAG, "\"1\"")
            )
                    .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

            // then
            verify(patchToAccountUpdateConverter).convert(new UUID(0, 1), 2, "1");
            verify(updateAccountCommand).execute(accountUpdate);
            verifyNoMoreInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }

        @Test
        void shouldNotUpdateAccountBecauseWrongEtag() throws Exception {
            // given
            Account accountUpdated = new AccountTestBuilder(1).withTestDefaults().scoring(2).build();
            var accountUpdate = getUpdate(1, 2, "99");

            when(patchToAccountUpdateConverter.convert(new UUID(0, 1), 2, "99"))
                    .thenReturn(accountUpdate);
            when(updateAccountCommand.execute(accountUpdate)).thenReturn(Result.fail(UpdateAccountCommand.Error.VERSION_NOT_MATCH));

            // when
            mvc.perform(
                    patch("/accounts/00000000-0000-0000-0000-000000000001")
                            .contentType("application/merge-patch+json")
                            .content(readFromResources("http/controller/account_controller_test/update_account_dto.json"))
                            .header(HttpHeaders.ETAG, "\"99\"")
            )
                    .andExpect(status().is(HttpStatus.PRECONDITION_FAILED.value()));

            // then
            verify(patchToAccountUpdateConverter).convert(new UUID(0, 1), 2, "99");
            verify(updateAccountCommand).execute(accountUpdate);
            verifyNoMoreInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }

        @Test
        void shouldNotUpdateAccountBecauseMissingScoring() throws Exception {
            mvc.perform(
                    patch("/accounts/00000000-0000-0000-0000-000000000001")
                            .contentType("application/merge-patch+json")
                            .content(readFromResources("http/controller/account_controller_test/update_account_dto_missing_scoring.json"))
                            .header(HttpHeaders.ETAG, "\"1\"")
            )
                    .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

            verifyNoInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }
    }

    @Nested
    @DisplayName("Error handling")
    class ErrorHandling {

        @Test
        void shouldNotGetAccount() throws Exception {
            // given
            AccountId accountId = AccountId.from(1);
            when(getAccountQuery.execute(accountId)).thenThrow(new RuntimeException("some exception"));

            // when
            mvc.perform(
                    get("/accounts/00000000-0000-0000-0000-000000000001"))
                    .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                    .andExpect(content().json(readFromResources("http/controller/account_controller_test/problem_dto.json")));

            // then
            verify(getAccountQuery).execute(accountId);
            verifyNoMoreInteractions(createAccountCommand, getAccountQuery, updateAccountCommand,
                    patchToAccountUpdateConverter, createAccountDtoToAccountConverter, accountToGetAccountDtoConverter);
        }


    }
}
