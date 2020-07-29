package com.marekhudyma.testcontainers.http.converter;

import com.marekhudyma.testcontainers.http.dto.CreateAccountDto;
import com.marekhudyma.testcontainers.domain.model.Account;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class AccountToAccountDtoConverter implements Converter<Account, CreateAccountDto> {

    @Override
    public CreateAccountDto convert(Account source) {
        return CreateAccountDto.builder()
                .name(source.getName())
                .scoring(source.getScoring())
                .build();
    }
}
