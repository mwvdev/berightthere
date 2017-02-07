package mwvdev.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidGenerator {

    public UUID generate() {
        return UUID.randomUUID();
    }

}
