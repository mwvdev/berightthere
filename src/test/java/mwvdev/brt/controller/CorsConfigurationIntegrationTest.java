package mwvdev.brt.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorsConfigurationIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void CanAddCors() {
        RequestEntity requestEntity = RequestEntity.get(getUri("api/trip/checkin")).header(HttpHeaders.ORIGIN, "http://example.org").build();
        ResponseEntity response = restTemplate.exchange(requestEntity, String.class);

        assertThat(HttpStatus.OK, is(response.getStatusCode()));
        assertThat("*", is(response.getHeaders().getAccessControlAllowOrigin()));
    }

}