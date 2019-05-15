package mwvdev.brt.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SecurityConfigurationIntegrationTest extends BaseIntegrationTest
{

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    void canCheckinAnynomously() throws IllegalStateException {
        TestRestTemplate restTemplate = getAnonymousRestTemplate();

        ResponseEntity response = restTemplate.getForEntity(getUri("api/trip/checkin"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void canAddLocationAnonymously() throws IllegalStateException {
        TestRestTemplate restTemplate = getAnonymousRestTemplate();

        ResponseEntity response = restTemplate.getForEntity(getUri("api/trip/ab7d8df0-e952-4956-8c38-0351b90fd045/addLocation/55.6745/12.56"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void canListLocations() throws IllegalStateException {
        TestRestTemplate restTemplate = getAnonymousRestTemplate();

        ResponseEntity response = restTemplate.getForEntity(getUri("api/trip/ab7d8df0-e952-4956-8c38-0351b90fd045/locations"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void canViewTrip() throws IllegalStateException {
        TestRestTemplate restTemplate = getAnonymousRestTemplate();

        ResponseEntity response = restTemplate.getForEntity(getUri("trip/ab7d8df0-e952-4956-8c38-0351b90fd045"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void canGetHealthAnynomously() throws IllegalStateException {
        TestRestTemplate restTemplate = getAnonymousRestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(getUri("actuator/health"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void getMetricsRefusesAcccessWhenAnonymous() throws IllegalStateException {
        TestRestTemplate restTemplate = getAnonymousRestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(getUri("actuator/metrics"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void getMetricsRefusesAcccessWhenCredentialsInvalid() throws IllegalStateException {
        TestRestTemplate restTemplate = getRestTemplateWithInvalidCredentials();

        ResponseEntity<String> response = restTemplate.getForEntity(getUri("actuator/metrics"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void canGetMetricsWhenAuthenticated() throws IllegalStateException {
        TestRestTemplate restTemplate = getRestTemplateWithValidCredentials();

        ResponseEntity<String> response = restTemplate.getForEntity(getUri("actuator/metrics"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void getPrometheusRefusesAcccessWhenAnonymous() throws IllegalStateException {
        TestRestTemplate restTemplate = getAnonymousRestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(getUri("actuator/prometheus"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void getPrometheusRefusesAcccessWhenCredentialsInvalid() throws IllegalStateException {
        TestRestTemplate restTemplate = getRestTemplateWithInvalidCredentials();

        ResponseEntity<String> response = restTemplate.getForEntity(getUri("actuator/prometheus"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void canGetPrometheusWhenAuthenticated() throws IllegalStateException {
        TestRestTemplate restTemplate = getRestTemplateWithValidCredentials();

        ResponseEntity<String> response = restTemplate.getForEntity(getUri("actuator/prometheus"), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    private TestRestTemplate getAnonymousRestTemplate() {
        return new TestRestTemplate();
    }

    private TestRestTemplate getRestTemplateWithValidCredentials() {
        SecurityProperties.User user = securityProperties.getUser();
        return new TestRestTemplate(user.getName(), user.getPassword());
    }

    private TestRestTemplate getRestTemplateWithInvalidCredentials() {
        return new TestRestTemplate("invalid", "invalid");
    }

}