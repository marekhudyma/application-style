package com.marekhudyma.style;

import com.marekhudyma.style.util.AbstractFunctionalTest;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static com.marekhudyma.style.util.TestResource.readFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountFunctionalTest extends AbstractFunctionalTest {

    @Test
    void shouldUpdateAccount() throws JSONException {
        // given
        createAccount();
        String etag = getEtag("00000000-0000-0000-0000-000000000001");

        // when
        ResponseEntity<String> response = getTestRestTemplate().exchange("/accounts/00000000-0000-0000-0000-000000000001",
                HttpMethod.PATCH,
                new HttpEntity<>(readFromResources("account_functional_test/patch_account_dto.json"), getPatchHeaders(etag)),
                String.class);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.NO_CONTENT.value());
        var actual = getAccount("00000000-0000-0000-0000-000000000001");
        var expected = readFromResources("account_functional_test/get_account_dto.json");
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    private void createAccount() {
        var json = readFromResources("account_functional_test/create_account_dto.json");
        ResponseEntity<String> response = getTestRestTemplate().exchange("/accounts",
                HttpMethod.POST,
                new HttpEntity<>(json, getPostHeaders()),
                String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    }

    private String getEtag(String id) {
        ResponseEntity<String> response = getTestRestTemplate().getForEntity("/accounts/{id}", String.class, id);
        return response.getHeaders().getETag();
    }

    private String getAccount(String id) {
        ResponseEntity<String> response = getTestRestTemplate().getForEntity("/accounts/{id}", String.class, id);
        return response.getBody();
    }

    private HttpHeaders getPostHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private HttpHeaders getPatchHeaders(String etag) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "merge-patch+json"));
        headers.add(HttpHeaders.ETAG, etag);
        return headers;
    }

}
