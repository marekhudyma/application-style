package com.marekhudyma.style.api.converter;

import com.marekhudyma.style.domain.command.UpdateAccountCommand;
import com.marekhudyma.style.domain.model.AccountId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PatchToAccountUpdateConverterTest {

    private PatchToAccountUpdateConverter underTest;

    @BeforeEach
    void setUp() {
        underTest = new PatchToAccountUpdateConverter();
    }

    @Test
    void shouldConvertPatchToAccountUpdate() {
        // when
        UpdateAccountCommand.AccountUpdate actual = underTest.convert(new UUID(0, 1), 1, "0");

        // then
        UpdateAccountCommand.AccountUpdate expected = new UpdateAccountCommand.AccountUpdate() {
            @Override
            public AccountId getId() {
                return AccountId.from(1);
            }

            @Override
            public int getScoring() {
                return 1;
            }

            @Override
            public String getVersion() {
                return "0";
            }
        };

        assertThat(actual).isEqualToComparingFieldByField(expected);
    }
}
