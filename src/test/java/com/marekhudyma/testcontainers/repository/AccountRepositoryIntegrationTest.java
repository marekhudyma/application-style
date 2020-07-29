package com.marekhudyma.testcontainers.repository;


import com.marekhudyma.testcontainers.domain.model.Account;
import com.marekhudyma.testcontainers.persistence.AccountRepository;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AccountRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    private String name;

    @BeforeEach
    void setUp() throws Exception {
        name = UUID.randomUUID().toString();
    }

    @Test
    void shouldCreateAccount() throws Exception {
        Account account = Account.builder().name(name).additionalInfo("additionalInfo").build();
        accountRepository.save(account);

        Account actual = accountRepository.findById(account.getId()).get();
        assertThat(actual).isEqualTo(account);
    }

    @Test
    void shouldNotCreateTwoAccountWithTheSameName() throws Exception {
        accountRepository.save(Account.builder().name(name).additionalInfo("additionalInfo").build());

        assertThrows(DataIntegrityViolationException.class,
                () -> accountRepository.save(Account.builder().name(name).additionalInfo("additionalInfo").build()));
    }

    @Test
    void shouldFindByName() throws Exception {
        Account account = Account.builder().name(name).additionalInfo("additionalInfo").build();
        accountRepository.save(account);

        Optional<Account> actual = accountRepository.findByName(name);
        assertThat(actual.get()).isEqualTo(account);
    }

    @Test
    void shouldNotFindByName() throws Exception {
        Optional<Account> actual = accountRepository.findByName(UUID.randomUUID().toString());
        assertThat(actual).isEmpty();
    }

}

