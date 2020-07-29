package com.marekhudyma.testcontainers.http.converter;

import com.marekhudyma.testcontainers.http.dto.CreateAccountDto;
import com.marekhudyma.testcontainers.http.dto.AccountDtoTestBuilder;
import com.marekhudyma.testcontainers.domain.model.Account;
import com.marekhudyma.testcontainers.domain.model.AccountTestBuilder;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


class AccountDtoToGetAccountConverterTest extends AbstractIntegrationTest {

    @Autowired
    private AccountDtoToAccountConverter accountDtoToAccountConverter;

    @Test
    void shouldConvert() {
        CreateAccountDto createAccountDto = new AccountDtoTestBuilder(1)
                .name("name.1")
                .scoring(1)
                .build();

        Account actual = accountDtoToAccountConverter.convert(createAccountDto);
        Account expected = new AccountTestBuilder(1)
                .name("name.1")
                .scoring(1)
                .build();

        assertThat(actual).isEqualTo(expected);
    }
}
