package com.marekhudyma.testcontainers.client.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import static com.marekhudyma.testcontainers.util.Resources.readFromResources;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@JsonTest
public class ExternalServiceResponseDtoTest {

    @Autowired
    private JacksonTester<ExternalServiceResponseDto> json;

    @Test
    public void serializeJson() throws Exception {
        ExternalServiceResponseDto additionalInfo = new ExternalServiceResponseDtoTestBuilder(1)
                .withTestDefaults().build();
        String expected = readFromResources("additionalInfo1.json");

        String actual = json.write(additionalInfo).getJson();
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void testDeserialize() throws Exception {
        ExternalServiceResponseDto expected = new ExternalServiceResponseDtoTestBuilder(1)
                .withTestDefaults().build();
        ExternalServiceResponseDto actual = json.parse(readFromResources("additionalInfo1.json"))
                .getObject();

        assertEquals("should be equal", expected, actual);
    }

}
