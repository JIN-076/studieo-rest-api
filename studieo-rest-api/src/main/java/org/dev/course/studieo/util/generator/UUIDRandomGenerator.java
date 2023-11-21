package org.dev.course.studieo.util.generator;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDRandomGenerator implements UUIDGenerator {

    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
