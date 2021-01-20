package com.marekhudyma.style.persistence;


import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.model.AccountId;
import com.marekhudyma.style.domain.model.AccountTestBuilder;
import com.marekhudyma.style.util.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AccountRepository2IntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected TransactionTemplate transactionTemplate;

    @Autowired
    private AccountRepository underTest;

    private int seed;

    @BeforeEach
    void setUp() {
        seed = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    }

    @Test
    void shouldCreateAccount() {
        // given
        Account account = new AccountTestBuilder(seed).withTestDefaults()
                .id(AccountId.from(seed))
                .version(null)
                .build();

        // when
        underTest.save(account);

        // then
        Optional<Account> actualOptional = underTest.findById(account.getId());
        Account expected = new AccountTestBuilder(seed).withTestDefaults()
                .id(AccountId.from(seed))
                .version(0)
                .build();
        assertThat(actualOptional).isPresent();
        assertThat(actualOptional.get()).isEqualTo(expected);
        assertThat(actualOptional.get().getCreatedAt()).isAfter(Instant.now().minusSeconds(60));
        assertThat(actualOptional.get().getUpdatedAt()).isAfter(Instant.now().minusSeconds(60));
    }

    @Test
    void shouldNotCreateTwoAccountWithTheSameId() {
        // given
        underTest.save(new AccountTestBuilder(seed).withTestDefaults()
                .id(AccountId.from(seed))
                .version(null)
                .build());

        // when, then
        assertThrows(DataIntegrityViolationException.class,
                () -> underTest.save(new AccountTestBuilder(seed).withTestDefaults()
                        .id(AccountId.from(seed))
                        .version(null)
                        .build()));
    }

    @Test
    void shouldNotSaveChangesWhenWrongVersion() {
        // given
        underTest.save(new AccountTestBuilder(seed).withTestDefaults()
                .id(AccountId.from(seed))
                .version(null)
                .build());

        // when, then
        assertThrows(ObjectOptimisticLockingFailureException.class,
                () -> underTest.save(new AccountTestBuilder(seed).withTestDefaults()
                        .id(AccountId.from(seed))
                        .version(999999)
                        .build()));
    }

    @Test
    void shouldIncrementVersionFieldWhenSaved() {
        // given
        final Account account = new AccountTestBuilder(seed).withTestDefaults()
                .id(AccountId.from(seed))
                .version(null)
                .build();

        // when
        executeInBlockingThread(() -> runInTransaction(() -> {
            underTest.saveAndFlush(account);
        }));

        executeInBlockingThread(() -> runInTransaction(() -> {
            Account a = underTest.findById(account.getId()).orElseThrow();
            a.setName("newName");
            underTest.saveAndFlush(a);
        }));

        Optional<Account> actualOptional = underTest.findById(account.getId());
        assertThat(actualOptional).isPresent();
        assertThat(actualOptional.get().getVersion()).isEqualTo(1);
    }

    protected void runInTransaction(Runnable runnable) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                runnable.run();
            }
        });
    }

    protected Thread startThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.start();
        return t;
    }

    protected Thread executeInBlockingThread(Runnable runnable) {
        Thread t = startThread(runnable);
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

}