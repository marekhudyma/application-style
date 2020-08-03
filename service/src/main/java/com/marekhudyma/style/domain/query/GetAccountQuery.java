package com.marekhudyma.style.domain.query;

import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.model.AccountId;
import com.marekhudyma.style.domain.query.GetAccountQuery.Error;
import com.marekhudyma.style.domain.query.util.Query;
import com.marekhudyma.style.domain.util.Result;
import com.marekhudyma.style.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetAccountQuery implements Query<AccountId, Result<Error, Account>> {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Result<Error, Account> execute(AccountId accountId) {
        return accountRepository.findById(accountId)
                .map(Result::<Error, Account>ok)
                .orElse(Result.fail(Error.ACCOUNT_NOT_FOUND));
    }

    public enum Error {
        ACCOUNT_NOT_FOUND
    }

}