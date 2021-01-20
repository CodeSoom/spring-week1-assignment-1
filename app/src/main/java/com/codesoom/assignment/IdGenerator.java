package com.codesoom.assignment;

public class IdGenerator {
    private static Long count = 1L;
    private IdGenerator() {
    }

    public static Long generate() {
        return count++;
    }
}
