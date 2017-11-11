package com.marekhudyma.testcontainers.controller.converter;

import com.marekhudyma.testcontainers.controller.dto.AccountDto;
import com.marekhudyma.testcontainers.model.Account;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class AccountToAccountDtoConverter implements Converter<Account, AccountDto> {

    @Override
    public AccountDto convert(Account source) {
        return AccountDto.builder()
                .name(source.getName())
                .scoring(source.getScoring())
                .build();
    }
}
