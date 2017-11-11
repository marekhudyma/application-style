package com.marekhudyma.testcontainers.controller;

import com.marekhudyma.testcontainers.controller.converter.AccountDtoToAccountConverter;
import com.marekhudyma.testcontainers.controller.converter.AccountToAccountDtoConverter;
import com.marekhudyma.testcontainers.controller.dto.AccountDto;
import com.marekhudyma.testcontainers.model.Account;
import com.marekhudyma.testcontainers.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Log4j
public class AccountController {

    private final AccountService accountService;

    private final AccountDtoToAccountConverter accountDtoToAccountConverter;

    private final AccountToAccountDtoConverter accountToAccountDtoConverter;

    @PostMapping
    public ResponseEntity<AccountDto> execute(@Valid @RequestBody AccountDto accountDto) {
        Account account = accountService.createAccount(accountDtoToAccountConverter.convert(accountDto));
        return ResponseEntity.status(CREATED).body(accountToAccountDtoConverter.convert(account));
    }

}
