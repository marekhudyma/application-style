package com.marekhudyma.style.domain.command;

import com.marekhudyma.style.domain.command.UpdateAccountCommand.AccountUpdate;
import com.marekhudyma.style.domain.command.UpdateAccountCommand.Error;
import com.marekhudyma.style.domain.command.util.Command;
import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.model.AccountId;
import com.marekhudyma.style.domain.util.Result;
import com.marekhudyma.style.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class UpdateAccountCommand implements Command<AccountUpdate, Result<Error, Account>> {

    private final AccountRepository accountRepository;

    public Result<Error, Account> execute(AccountUpdate accountUpdate) {
        return accountRepository.findById(accountUpdate.getId()).map(a -> {
            if (accountUpdate.getVersion().equals(a.getVersion().toString())) {
                a.setScoring(accountUpdate.getScoring());
                return save(a);
            } else {
                return Result.<Error, Account>fail(Error.VERSION_NOT_MATCH);
            }
        }).orElse(Result.fail(Error.ACCOUNT_NOT_FOUND));
    }

    private Result<Error, Account> save(Account account) {
        try {
            return Result.ok(accountRepository.save(account));
        } catch (ObjectOptimisticLockingFailureException e) {
            return Result.fail(Error.VERSION_NOT_MATCH);
        }

    }

    public enum Error {
        ACCOUNT_NOT_FOUND,
        VERSION_NOT_MATCH
    }

    public interface AccountUpdate {

        AccountId getId();

        int getScoring();

        String getVersion();

    }

}
