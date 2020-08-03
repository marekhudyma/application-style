package com.marekhudyma.style.domain.command;

import com.marekhudyma.style.domain.command.CreateAccountCommand.Error;
import com.marekhudyma.style.domain.command.util.Command;
import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.util.Result;
import com.marekhudyma.style.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAccountCommand implements Command<Account, Result<Error, Account>> {

    private final AccountRepository accountRepository;

    public Result<Error, Account> execute(Account account) {
        return accountRepository.findById(account.getId())
                .map(a -> Result.fail(a, Error.ACCOUNT_ALREADY_EXIST))
                .orElseGet(() -> save(account));
    }

    private Result<Error, Account> save(Account account) {
        try {
            return Result.ok(accountRepository.save(account));
        } catch (DataIntegrityViolationException e) {
            return Result.fail(account, Error.ACCOUNT_ALREADY_EXIST);
        }
    }

    public enum Error {
        ACCOUNT_ALREADY_EXIST
    }

}