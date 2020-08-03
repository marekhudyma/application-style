package com.marekhudyma.style.api.converter;

import com.marekhudyma.style.api.dto.GetAccountDto;
import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.model.AccountTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountToGetAccountDtoConverterTest {

    private AccountToGetAccountDtoConverter underTest;

    @BeforeEach
    void setUp() {
        underTest = new AccountToGetAccountDtoConverter();
    }

    @Test
    void shouldConvertAccountToGetAccountDto() {
        // given
        Account account = new AccountTestBuilder(1).withTestDefaults().build();

        // when
        GetAccountDto actual = underTest.convert(account);

        // then
        GetAccountDto expected = new GetAccountDto(new UUID(0, 1), "name.1", 1);
        assertThat(actual).isEqualTo(expected);
    }
}
