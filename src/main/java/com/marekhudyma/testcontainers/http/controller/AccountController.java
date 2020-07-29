package com.marekhudyma.testcontainers.http.controller;

import com.marekhudyma.testcontainers.domain.command.CreateAccountCommand;
import com.marekhudyma.testcontainers.http.converter.AccountDtoToAccountConverter;
import com.marekhudyma.testcontainers.http.converter.AccountToAccountDtoConverter;
import com.marekhudyma.testcontainers.http.dto.CreateAccountDto;
import com.marekhudyma.testcontainers.domain.model.Account;
import com.marekhudyma.testcontainers.domain.service.AccountService;
import com.sun.codemodel.internal.JSwitch;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class AccountController {

    private final CreateAccountCommand createAccountCommand;

    private final AccountDtoToAccountConverter accountDtoToAccountConverter;

    private final AccountToAccountDtoConverter accountToAccountDtoConverter;

    @PostMapping
    public ResponseEntity<CreateAccountDto> create(@Valid @RequestBody CreateAccountDto createAccountDto) {
//        return getBoardByInviteQuery.run(inviteId).map(
//            error -> switch (error) {
//                case INVITE_USED, INVITE_NOT_FOUND, BOARD_DELETED -> ResponseEntity.notFound().build();
//            },
//            board -> ResponseEntity.ok(new InviteDto(new BoardDto(board.getPublicId().toString(), board.getName())))
//        );


//        Account account = createAccountCommand.createAccount(accountDtoToAccountConverter.convert(createAccountDto));
//        return ResponseEntity.status(CREATED).body(accountToAccountDtoConverter.convert(account));

        createAccountCommand.execute(accountDtoToAccountConverter.convert(createAccountDto))
            .map(error -> switch (error) {
                case ACCOUNT_ALREADY_EXIST:
                    ResponseEntity.notFound().build();
            },
                 account -> {
                     ResponseEntity.ok("");
                 }

            );

    }

}
