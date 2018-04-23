package mwvdev.integration;

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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorsConfigurationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void CanAddCors() {
        RequestEntity requestEntity = RequestEntity.get(getUri("api/trip/checkin")).header(HttpHeaders.ORIGIN, "http://example.org").build();
        ResponseEntity response = restTemplate.exchange(requestEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("*", response.getHeaders().getAccessControlAllowOrigin());
    }

}
