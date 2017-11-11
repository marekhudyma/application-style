package com.marekhudyma.testcontainers.repository;


import com.marekhudyma.testcontainers.model.Account;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;


public class AccountRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    private String name;

    @Before
    public void setUp() throws Exception {
        name = UUID.randomUUID().toString();
    }

    @Test
    public void shouldCreateAccount() throws Exception {
        Account account = Account.builder().name(name).additionalInfo("additionalInfo").build();
        accountRepository.save(account);

        Account actual = accountRepository.findOne(account.getId());
        assertEquals("should be equal", account, actual);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotCreateTwoAccountWithTheSameName() throws Exception {
        accountRepository.save(Account.builder().name(name).additionalInfo("additionalInfo").build());
        accountRepository.save(Account.builder().name(name).additionalInfo("additionalInfo").build());
    }

    @Test
    public void shouldFindByName() throws Exception {
        Account account = Account.builder().name(name).additionalInfo("additionalInfo").build();
        accountRepository.save(account);

        Optional<Account> actual = accountRepository.findByName(name);
        assertEquals("should be equal", Optional.of(account), actual);
    }

    @Test
    public void shouldNotFindByName() throws Exception {
        Optional<Account> expected = Optional.empty();
        Optional<Account> actual = accountRepository.findByName(UUID.randomUUID().toString());
        assertEquals("should be equal", expected, actual);
    }

}

