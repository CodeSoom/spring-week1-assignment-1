package com.codesoom.assignment.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {

    private static final IdGenerator instance = new IdGenerator();
    private static final AtomicLong atomicLong = new AtomicLong(1L);

    private IdGenerator() {
    }

    public static IdGenerator getInstance() {
        return instance;
    }

    public Long allocateId() {
        return atomicLong.getAndIncrement();
    }
}
