package com.marekhudyma.style.api.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.marekhudyma.style.util.TestResources.readFromResources;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@JsonTest
class PatchAccountDtoTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSerialize() throws Exception {
        // given
        var patchAccountDto = new PatchAccountDto(1);

        // when
        var actual = objectMapper.writeValueAsString(patchAccountDto);

        // then
        var expected = readFromResources("http/dto/patch_account_dto_test/patch_account_dto.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);

    }

    @Test
    public void testDeserialize() throws Exception {
        // given
        var json = readFromResources("http/dto/patch_account_dto_test/patch_account_dto.json");

        // when
        var actual = objectMapper.readValue(json, PatchAccountDto.class);

        // then
        var expected = new PatchAccountDto(1);
        assertThat(actual).isEqualTo(expected);
    }

}