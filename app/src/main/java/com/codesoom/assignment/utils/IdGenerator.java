package com.codesoom.assignment.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    private static final AtomicLong atomicLong = new AtomicLong(1L);

    public Long allocateId() {
        return atomicLong.getAndIncrement();
    }
}
