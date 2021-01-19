package com.codesoom.assignment.util;

public class IdGenerator {
    private Long id;

    public IdGenerator() {
        this.id = 1L;
    }

    public Long generate() {
        return id++;
    }
}
