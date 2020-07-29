package com.marekhudyma.testcontainers.http.converter;

import com.marekhudyma.testcontainers.http.dto.CreateAccountDto;
import com.marekhudyma.testcontainers.http.dto.AccountDtoTestBuilder;
import com.marekhudyma.testcontainers.domain.model.Account;
import com.marekhudyma.testcontainers.domain.model.AccountTestBuilder;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


class AccountToCreateAccountDtoConverterTest extends AbstractIntegrationTest {

    @Autowired
    private AccountToAccountDtoConverter accountToAccountDtoConverter;

    @Test
    void shouldConvert() {
        Account account = new AccountTestBuilder(1).withTestDefaults().build();
        CreateAccountDto expected = new AccountDtoTestBuilder(1).withTestDefaults().build();

        CreateAccountDto createAccountDto = accountToAccountDtoConverter.convert(account);

        assertThat(createAccountDto).isEqualTo(expected);
    }
}
