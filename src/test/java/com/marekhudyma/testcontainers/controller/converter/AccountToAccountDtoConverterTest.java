package com.marekhudyma.testcontainers.controller.converter;

import com.marekhudyma.testcontainers.controller.dto.AccountDto;
import com.marekhudyma.testcontainers.controller.dto.AccountDtoTestBuilder;
import com.marekhudyma.testcontainers.model.Account;
import com.marekhudyma.testcontainers.model.AccountTestBuilder;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


class AccountToAccountDtoConverterTest extends AbstractIntegrationTest {

    @Autowired
    private AccountToAccountDtoConverter accountToAccountDtoConverter;

    @Test
    void shouldConvert() {
        Account account = new AccountTestBuilder(1).withTestDefaults().build();
        AccountDto expected = new AccountDtoTestBuilder(1).withTestDefaults().build();

        AccountDto accountDto = accountToAccountDtoConverter.convert(account);

        assertThat(accountDto).isEqualTo(expected);
    }
}
