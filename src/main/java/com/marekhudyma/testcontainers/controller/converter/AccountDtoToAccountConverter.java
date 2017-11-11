package com.marekhudyma.testcontainers.controller.converter;

import com.marekhudyma.testcontainers.controller.dto.AccountDto;
import com.marekhudyma.testcontainers.model.Account;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class AccountDtoToAccountConverter implements Converter<AccountDto, Account> {

    @Override
    public Account convert(AccountDto source) {
        return Account.builder()
                .name(source.getName())
                .scoring(source.getScoring())
                .build();
    }
}