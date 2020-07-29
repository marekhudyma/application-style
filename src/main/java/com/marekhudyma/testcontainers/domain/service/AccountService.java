package com.marekhudyma.testcontainers.domain.service;

import com.marekhudyma.testcontainers.domain.exception.AccountExistException;
import com.marekhudyma.testcontainers.domain.model.Account;
import com.marekhudyma.testcontainers.persistence.AccountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account createAccount(Account account) {
        String name = account.getName();
        Optional<Account> accountByName = accountRepository.findByName(name);
        accountByName.ifPresent(x -> {
            throw new AccountExistException(name);
        });
        return accountRepository.save(account);
    }

}