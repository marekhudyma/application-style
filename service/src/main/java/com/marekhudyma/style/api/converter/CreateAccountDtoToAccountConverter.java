package com.marekhudyma.style.api.converter;

import com.marekhudyma.style.api.dto.CreateAccountDto;
import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.model.AccountId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class CreateAccountDtoToAccountConverter implements Converter<CreateAccountDto, Account> {

    @Override
    public Account convert(CreateAccountDto source) {
        return Account.builder()
                .id(AccountId.from(source.id()))
                .name(source.name())
                .scoring(source.scoring())
                .build();
    }
}