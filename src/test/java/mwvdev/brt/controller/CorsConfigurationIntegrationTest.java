package mwvdev.brt.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CorsConfigurationIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void CanAddCors() {
        RequestEntity requestEntity = RequestEntity.get(getUri("api/trip/checkin")).header(HttpHeaders.ORIGIN, "http://example.org").build();
        ResponseEntity response = restTemplate.exchange(requestEntity, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getHeaders().getAccessControlAllowOrigin(), is("*"));
    }

}