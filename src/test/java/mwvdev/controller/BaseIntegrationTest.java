package mwvdev.controller;

import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

class BaseIntegrationTest {

    @LocalServerPort
    private int port;

    protected URI getUri(String path) {
        return UriComponentsBuilder
                .fromHttpUrl("http://localhost:{port}/{path}")
                .buildAndExpand(port, path)
                .toUri();
    }

}