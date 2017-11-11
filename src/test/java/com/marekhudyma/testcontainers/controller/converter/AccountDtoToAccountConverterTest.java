package com.marekhudyma.testcontainers.controller.converter;

import com.marekhudyma.testcontainers.controller.dto.AccountDto;
import com.marekhudyma.testcontainers.controller.dto.AccountDtoTestBuilder;
import com.marekhudyma.testcontainers.model.Account;
import com.marekhudyma.testcontainers.model.AccountTestBuilder;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.TestCase.assertEquals;


public class AccountDtoToAccountConverterTest extends AbstractIntegrationTest {

    @Autowired
    private AccountDtoToAccountConverter accountDtoToAccountConverter;

    @Test
    public void shouldConvert() {
        AccountDto accountDto = new AccountDtoTestBuilder(1)
                .name("name.1")
                .scoring(1)
                .build();

        Account actual = accountDtoToAccountConverter.convert(accountDto);
        Account expected = new AccountTestBuilder(1)
                .name("name.1")
                .scoring(1)
                .build();

        assertEquals("should be equal", expected, actual);
    }
}
