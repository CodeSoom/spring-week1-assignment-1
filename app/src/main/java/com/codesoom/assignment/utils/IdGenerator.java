package com.codesoom.assignment.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    private AtomicLong id = new AtomicLong(0);

    public Long newId() {
        return id.incrementAndGet();
    }

}

