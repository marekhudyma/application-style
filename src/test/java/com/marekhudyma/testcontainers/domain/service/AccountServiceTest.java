package com.marekhudyma.testcontainers.domain.service;

import com.marekhudyma.testcontainers.domain.exception.AccountExistException;
import com.marekhudyma.testcontainers.domain.model.Account;
import com.marekhudyma.testcontainers.domain.model.AccountTestBuilder;
import com.marekhudyma.testcontainers.persistence.AccountRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Log4j2
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {



    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PlatformTransactionManager transactionManager;



    @InjectMocks
    private AccountService unterTest;

    @Test
    void shouldSaveAccountToDatabase() {
        //TODO HUDYMA
//        Account account = new AccountTestBuilder(1).withTestDefaults().id(null).name("name.1").build();
//        Account expected = new AccountTestBuilder(1).withTestDefaults().id(1L).name("name.1").build();
//
//        when(accountRepository.findByName("name.1")).thenReturn(Optional.empty());
//        when(externalServiceClient.getExternal("name.1"))
//                .thenReturn(Optional.of(new ExternalServiceResponseDtoTestBuilder(1).withTestDefaults().build()));
//        when(accountRepository.save(account)).thenReturn(expected);
//
//        Account actual = unterTest.createAccount(account);
//
//        assertThat(actual).isEqualTo(expected);
//        verify(accountRepository).findByName("name.1");
//        verify(externalServiceClient).getExternal("name.1");
//        verify(accountRepository).save(account);
//        verify(queuePublisher).publish(new MessageDto(1L));
//        verifyNoMoreInteractions(accountRepository, accountRepository);
    }

    @Test
    void shouldNotSaveAccountWhenProblemWithCommunicationWithExternalService() {
        //TODO HUDYMA
//        Account expected = new AccountTestBuilder(1).withTestDefaults().id(null).build();
//        when(accountRepository.findByName("name.1")).thenReturn(Optional.empty());
//        when(externalServiceClient.getExternal("name.1")).thenReturn(Optional.empty());
//
//        assertThrows(MissingAdditionalInformationException.class, () -> unterTest.createAccount(expected));
//
//        verifyNoMoreInteractions(accountRepository, accountRepository);
    }

    @Test
    void shouldNotSaveAccountWhenEntityExists() {
        Account expected = new AccountTestBuilder(1).withTestDefaults().id(null).build();
        when(accountRepository.findByName("name.1")).thenReturn(
                Optional.of(new AccountTestBuilder(1).withTestDefaults().id(null).name("name.1").build()));

        assertThrows(AccountExistException.class, () -> unterTest.createAccount(expected));

        verifyNoMoreInteractions(accountRepository, accountRepository);
    }

}
