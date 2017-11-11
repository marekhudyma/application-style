package com.marekhudyma.testcontainers.service;

import com.marekhudyma.testcontainers.client.ExternalServiceClient;
import com.marekhudyma.testcontainers.client.dto.ExternalServiceResponseDtoTestBuilder;
import com.marekhudyma.testcontainers.exception.AccountExistException;
import com.marekhudyma.testcontainers.exception.MissingAdditionalInformationException;
import com.marekhudyma.testcontainers.model.Account;
import com.marekhudyma.testcontainers.model.AccountTestBuilder;
import com.marekhudyma.testcontainers.queue.MessageDto;
import com.marekhudyma.testcontainers.queue.QueuePublisher;
import com.marekhudyma.testcontainers.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private ExternalServiceClient externalServiceClient;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private QueuePublisher queuePublisher;

    @InjectMocks
    private AccountService unterTest;

    @Test
    public void shouldSaveAccountToDatabase() {
        Account account = new AccountTestBuilder(1).withTestDefaults().id(null).name("name.1").build();
        Account expected = new AccountTestBuilder(1).withTestDefaults().id(1L).name("name.1").build();

        when(accountRepository.findByName("name.1")).thenReturn(Optional.empty());
        when(externalServiceClient.getExternal("name.1"))
                .thenReturn(Optional.of(new ExternalServiceResponseDtoTestBuilder(1).withTestDefaults().build()));
        when(accountRepository.save(account)).thenReturn(expected);

        Account actual = unterTest.createAccount(account);

        assertEquals("should be equal", expected, actual);
        verify(accountRepository).findByName("name.1");
        verify(externalServiceClient).getExternal("name.1");
        verify(accountRepository).save(account);
        verify(queuePublisher).publish(new MessageDto(1L));
        verifyNoMoreInteractions(accountRepository, accountRepository);
    }

    @Test(expected = MissingAdditionalInformationException.class)
    public void shouldNotSaveAccountWhenProblemWithCommunicationWithExternalService() {
        Account expected = new AccountTestBuilder(1).withTestDefaults().id(null).build();
        when(accountRepository.findByName("name.1")).thenReturn(Optional.empty());
        when(externalServiceClient.getExternal("name.1")).thenReturn(Optional.empty());

        unterTest.createAccount(expected);
    }

    @Test(expected = AccountExistException.class)
    public void shouldNotSaveAccountWhenEntityExists() {
        Account expected = new AccountTestBuilder(1).withTestDefaults().id(null).build();
        when(accountRepository.findByName("name.1")).thenReturn(
                Optional.of(new AccountTestBuilder(1).withTestDefaults().id(null).name("name.1").build()));

        unterTest.createAccount(expected);
    }

}
