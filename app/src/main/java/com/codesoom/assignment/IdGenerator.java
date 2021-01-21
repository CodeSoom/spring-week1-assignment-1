package com.codesoom.assignment;

public class IdGenerator {
    private Long count = 0L;

    public Long generate() {
        return count++;
    }
}
