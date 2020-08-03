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
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static com.marekhudyma.style.domain.command.CreateAccountCommand.Error.ACCOUNT_ALREADY_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateAccountCommandTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CreateAccountCommand underTest;

    @Test
    void shouldCreateAccount() {
        // given
        var name = "name";
        Account account = new AccountTestBuilder(1).withTestDefaults().name(name).build();
        when(accountRepository.findById(AccountId.from(1))).thenReturn(Optional.empty());
        when(accountRepository.save(account)).thenReturn(account);

        // when
        var actual = underTest.execute(account);

        // then
        assertThat(actual).isEqualTo(Result.ok(account));
        verify(accountRepository).findById(AccountId.from(1));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldReturnAccountAlreadyExist() {
        // given
        var name = "name";
        Account account = new AccountTestBuilder(1).withTestDefaults().name(name).build();
        when(accountRepository.findById(AccountId.from(1))).thenReturn(Optional.of(account));

        // when
        var actual = underTest.execute(account);

        // then
        assertThat(actual).isEqualTo(Result.fail(account, ACCOUNT_ALREADY_EXIST));
        verify(accountRepository).findById(AccountId.from(1));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldReturnAccountAlreadyExistWhenDataIntegrityViolationException() {
        // given
        var name = "name";
        Account account = new AccountTestBuilder(1).withTestDefaults().name(name).build();
        when(accountRepository.findById(AccountId.from(1))).thenReturn(Optional.empty());
        when(accountRepository.save(account)).thenThrow(new DataIntegrityViolationException("fake exception"));

        // when
        var actual = underTest.execute(account);

        // then
        assertThat(actual).isEqualTo(Result.fail(account, ACCOUNT_ALREADY_EXIST));
        verify(accountRepository).findById(AccountId.from(1));
        verify(accountRepository).save(account);
        verifyNoMoreInteractions(accountRepository);
    }

}