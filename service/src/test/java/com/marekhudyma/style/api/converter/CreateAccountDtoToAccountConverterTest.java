package com.marekhudyma.style.api.converter;

import com.marekhudyma.style.api.dto.CreateAccountDto;
import com.marekhudyma.style.domain.model.Account;
import com.marekhudyma.style.domain.model.AccountId;
import com.marekhudyma.style.domain.model.AccountTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CreateAccountDtoToAccountConverterTest {

    private CreateAccountDtoToAccountConverter underTest;

    @BeforeEach
    void setUp() {
        underTest = new CreateAccountDtoToAccountConverter();
    }

    @Test
    void shouldConvertCreateAccountDtoToAccount() {
        // given
        CreateAccountDto createAccountDto = new CreateAccountDto(new UUID(0, 1), "name.1", 1);

        // when
        Account actual = underTest.convert(createAccountDto);

        // then
        Account expected = new AccountTestBuilder(1).withTestDefaults()
                .id(AccountId.from(1))
                .createdAt(null)
                .updatedAt(null)
                .name("name.1")
                .scoring(1)
                .version(null)
                .build();

        assertThat(actual).isEqualTo(expected);
    }
}
