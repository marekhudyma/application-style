package com.marekhudyma.style.api.controller;

import com.marekhudyma.style.api.converter.PatchToAccountUpdateConverter;
import com.marekhudyma.style.api.dto.CreateAccountDto;
import com.marekhudyma.style.api.dto.GetAccountDto;
import com.marekhudyma.style.api.dto.PatchAccountDto;
import com.marekhudyma.style.domain.command.CreateAccountCommand;
import com.marekhudyma.style.domain.command.UpdateAccountCommand;
import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.model.AccountId;
import com.marekhudyma.style.domain.query.GetAccountQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@Log4j2
public class AccountController {

    private final static String PATCH_HEADER = "application/merge-patch+json";

    private final CreateAccountCommand createAccountCommand;

    private final GetAccountQuery getAccountQuery;

    private final UpdateAccountCommand updateAccountCommand;

    private final ConversionService conversionService;

    private final PatchToAccountUpdateConverter patchToAccountUpdateConverter;

    @PostMapping(path = "/accounts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> create(@Valid @RequestBody CreateAccountDto createAccountDto,
                             UriComponentsBuilder uriComponentsBuilder) {

        return createAccountCommand.execute(Objects.requireNonNull(conversionService.convert(createAccountDto, Account.class)))
                .map((account, error) -> switch (error) {
                            case ACCOUNT_ALREADY_EXIST -> ResponseEntity.status(HttpStatus.CONFLICT)
                                    .body(conversionService.convert(account, GetAccountDto.class));
                        },
                        account -> ResponseEntity
                                .created(uriComponentsBuilder.path("/{id}").buildAndExpand(account.getId()).toUri())
                                .body(conversionService.convert(account, GetAccountDto.class))
                );
    }

    @GetMapping(path = "/accounts/{accountId}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> getById(@PathVariable UUID accountId) {
        return getAccountQuery.execute(AccountId.from(accountId))
                .map(error -> switch (error) {
                            case ACCOUNT_NOT_FOUND -> ResponseEntity.notFound().build();
                        },
                        account -> ResponseEntity
                                .ok()
                                .eTag(account.getVersion().toString())
                                .body(conversionService.convert(account, GetAccountDto.class))


                );
    }

    @PatchMapping(value = "/accounts/{accountId}", consumes = PATCH_HEADER)
    public ResponseEntity<?> patch(@PathVariable UUID accountId,
                                   @Valid @RequestBody PatchAccountDto patchAccountDto,
                                   @RequestHeader(name = HttpHeaders.ETAG) String etag) {

        String etagCleaned = etag.replaceAll("\"", ""); // clean from outer quotes
        return updateAccountCommand
                .execute(patchToAccountUpdateConverter.convert(accountId, patchAccountDto.scoring(), etagCleaned))
                .map(error -> switch (error) {
                            case ACCOUNT_NOT_FOUND -> ResponseEntity.notFound().build();
                            case VERSION_NOT_MATCH -> ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
                        },
                        account -> ResponseEntity.noContent().build());
    }

}