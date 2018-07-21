package mwvdev.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidGeneratorImpl implements UuidGenerator {

    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }

}