package com.marekhudyma.style.api.converter;

import com.marekhudyma.style.api.dto.GetAccountDto;
import com.marekhudyma.style.domain.model.Account;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class AccountToGetAccountDtoConverter implements Converter<Account, GetAccountDto> {

    @Override
    public GetAccountDto convert(Account source) {
        return new GetAccountDto(source.getId().getValue(), source.getName(), source.getScoring());
    }
}