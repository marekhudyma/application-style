package com.marekhudyma.style.domain.command;

import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.model.AccountId;
import com.marekhudyma.style.domain.model.AccountTestBuilder;
import com.marekhudyma.style.domain.util.Result;
import com.marekhudyma.style.persistence.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static com.marekhudyma.style.domain.command.UpdateAccountCommand.Error.ACCOUNT_NOT_FOUND;
import static com.marekhudyma.style.domain.command.UpdateAccountCommand.Error.VERSION_NOT_MATCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UpdateAccountCommandTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private UpdateAccountCommand underTest;

    @Test
    void shouldUpdateAccount() {
        // given
        var accountUpdate = getUpdate(1, 2, "1");
        Account account = new AccountTestBuilder(1).withTestDefaults().build();
        Account accountUpdated = new AccountTestBuilder(1).withTestDefaults().scoring(2).build();
        when(accountRepository.findById(AccountId.from(1))).thenReturn(Optional.of(account));
        when(accountRepository.save(accountUpdated)).thenReturn(accountUpdated);

        // when
        var actual = underTest.execute(accountUpdate);

        // then
        assertThat(actual).isEqualTo(Result.ok(account));
        verify(accountRepository).findById(AccountId.from(1));
        verify(accountRepository).save(accountUpdated);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldNotUpdateAccountBecauseNotFound() {
        // given
        var accountUpdate = getUpdate(1, 2, "1");
        when(accountRepository.findById(AccountId.from(1))).thenReturn(Optional.empty());

        // when
        var actual = underTest.execute(accountUpdate);

        // then
        assertThat(actual).isEqualTo(Result.fail(ACCOUNT_NOT_FOUND));
        verify(accountRepository).findById(AccountId.from(1));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldNotUpdateAccountBecauseWrongVersion() {
        // given
        var accountUpdate = getUpdate(1, 2, "20");
        Account account = new AccountTestBuilder(1).withTestDefaults().build();
        when(accountRepository.findById(AccountId.from(1))).thenReturn(Optional.of(account));

        // when
        var actual = underTest.execute(accountUpdate);

        // then
        assertThat(actual).isEqualTo(Result.fail(VERSION_NOT_MATCH));
        verify(accountRepository).findById(AccountId.from(1));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldNotUpdateAccountBecauseObjectOptimisticLockingFailureException() {
        // given
        var accountUpdate = getUpdate(1, 2, "1");
        Account account = new AccountTestBuilder(1).withTestDefaults().build();
        Account accountUpdated = new AccountTestBuilder(1).withTestDefaults().scoring(2).build();
        when(accountRepository.findById(AccountId.from(1))).thenReturn(Optional.of(account));
        when(accountRepository.save(accountUpdated))
                .thenThrow(new ObjectOptimisticLockingFailureException("ObjectOptimisticLockingFailureException", account));

        // when
        var actual = underTest.execute(accountUpdate);

        // then
        assertThat(actual).isEqualTo(Result.fail(VERSION_NOT_MATCH));
        verify(accountRepository).findById(AccountId.from(1));
        verify(accountRepository).save(accountUpdated);
        verifyNoMoreInteractions(accountRepository);
    }

    private UpdateAccountCommand.AccountUpdate getUpdate(int id, int scoring, String etag) {
        return new UpdateAccountCommand.AccountUpdate() {
            @Override
            public AccountId getId() {
                return AccountId.from(id);
            }

            @Override
            public int getScoring() {
                return scoring;
            }

            @Override
            public String getVersion() {
                return etag;
            }
        };

    }

}