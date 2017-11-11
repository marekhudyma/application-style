package com.marekhudyma.testcontainers.service;

import com.marekhudyma.testcontainers.client.ExternalServiceClient;
import com.marekhudyma.testcontainers.client.dto.ExternalServiceResponseDto;
import com.marekhudyma.testcontainers.exception.AccountExistException;
import com.marekhudyma.testcontainers.exception.MissingAdditionalInformationException;
import com.marekhudyma.testcontainers.model.Account;
import com.marekhudyma.testcontainers.queue.MessageDto;
import com.marekhudyma.testcontainers.queue.QueuePublisher;
import com.marekhudyma.testcontainers.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final ExternalServiceClient externalServiceClient;

    private final AccountRepository accountRepository;

    private final PlatformTransactionManager transactionManager;

    private final QueuePublisher queuePublisher;

    public Account createAccount(Account account) {
        String name = account.getName();

        Optional<Account> accountByName = accountRepository.findByName(name);
        accountByName.ifPresent(x -> {
            throw new AccountExistException(name);
        });

        Optional<ExternalServiceResponseDto> externalServiceResponseDtoOptional =
                externalServiceClient.getExternal(name);

        if (externalServiceResponseDtoOptional.isPresent()) {
            ExternalServiceResponseDto externalServiceResponseDto = externalServiceResponseDtoOptional.get();
            account.setAdditionalInfo(externalServiceResponseDto.getAdditionalInfo());
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            return transactionTemplate.execute(transactionStatus -> saveAccount(account));
        } else {
            throw new MissingAdditionalInformationException(name);
        }
    }

    private Account saveAccount(Account account) {
        Account accountFromDb = accountRepository.save(account);
        queuePublisher.publish(new MessageDto(accountFromDb.getId()));
        return accountFromDb;
    }

}
