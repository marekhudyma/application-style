package com.marekhudyma.testcontainers.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marekhudyma.testcontainers.util.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

import static com.marekhudyma.testcontainers.util.Resources.readFromResources;
import static org.assertj.core.api.Assertions.assertThat;


class ExternalServiceResponseDtoTest extends AbstractIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializeJson() throws Exception {
        ExternalServiceResponseDto additionalInfo = new ExternalServiceResponseDtoTestBuilder(1)
                .withTestDefaults().build();
        String expected = readFromResources("additionalInfo1.json");

        String actual = objectMapper.writeValueAsString(additionalInfo);
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    void testDeserialize() throws Exception {
        ExternalServiceResponseDto expected = new ExternalServiceResponseDtoTestBuilder(1)
                .withTestDefaults().build();
        ExternalServiceResponseDto actual =
                objectMapper.readValue(readFromResources("additionalInfo1.json"), ExternalServiceResponseDto.class);

        assertThat(actual).isEqualTo(expected);
    }

}
