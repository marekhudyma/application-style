package com.marekhudyma.testcontainers.domain.command;

import com.marekhudyma.testcontainers.domain.command.CreateAccountCommand.Error;
import com.marekhudyma.testcontainers.domain.command.util.Command;
import com.marekhudyma.testcontainers.domain.command.util.Result;
import com.marekhudyma.testcontainers.domain.model.Account;
import com.marekhudyma.testcontainers.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAccountCommand implements Command<Account, Result<Error, Account>> {

  private final AccountRepository accountRepository;

  public Result<Error, Account> execute(Account account) {
    return accountRepository.findByName(account.getName())
        .map(a -> Result.<Error, Account>fail(Error.ACCOUNT_ALREADY_EXIST))
        .orElse(Result.ok(accountRepository.save(account)));
  }

  public enum Error {
    ACCOUNT_ALREADY_EXIST
  }

}
