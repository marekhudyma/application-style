package com.marekhudyma.testcontainers.http.converter;

import com.marekhudyma.testcontainers.http.dto.CreateAccountDto;
import com.marekhudyma.testcontainers.domain.model.Account;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class AccountDtoToAccountConverter implements Converter<CreateAccountDto, Account> {

    @Override
    public Account convert(CreateAccountDto source) {
        return Account.builder()
                .name(source.getName())
                .scoring(source.getScoring())
                .build();
    }
}