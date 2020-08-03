package com.marekhudyma.style.api.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static com.marekhudyma.style.util.TestResources.readFromResources;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@JsonTest
class CreateAccountDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSerialize() throws Exception {
        // given
        var createAccountDto = new CreateAccountDto(new UUID(0, 1), "name.1", 1);

        // when
        var actual = objectMapper.writeValueAsString(createAccountDto);

        // then
        var expected = readFromResources("http/dto/create_account_dto_test/create_account_dto.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @Test
    public void testDeserialize() throws Exception {
        // given
        var json = readFromResources("http/dto/create_account_dto_test/create_account_dto.json");

        // when
        var actual = objectMapper.readValue(json, CreateAccountDto.class);

        // then
        var expected = new CreateAccountDto(new UUID(0, 1), "name.1", 1);
        assertThat(actual).isEqualTo(expected);
    }

}