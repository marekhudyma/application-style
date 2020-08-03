package com.marekhudyma.style.domain.query;

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

import java.util.Optional;

import static com.marekhudyma.style.domain.query.GetAccountQuery.Error.ACCOUNT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAccountQueryTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private GetAccountQuery underTest;

    @Test
    void shouldGetAccount() {
        // given
        AccountId id = AccountId.from(1);
        Account account = new AccountTestBuilder(1).withTestDefaults()
                .id(id)
                .build();
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        // when
        var actual = underTest.execute(id);

        // then
        assertThat(actual).isEqualTo(Result.ok(account));
        verify(accountRepository).findById(id);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldNotGetAccount() {
        // given
        AccountId id = AccountId.from(1);
        Account account = new AccountTestBuilder(1).withTestDefaults()
                .id(id)
                .build();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        // when
        var actual = underTest.execute(id);

        // then
        assertThat(actual).isEqualTo(Result.fail(ACCOUNT_NOT_FOUND));
        verify(accountRepository).findById(id);
        verifyNoMoreInteractions(accountRepository);
    }
}
