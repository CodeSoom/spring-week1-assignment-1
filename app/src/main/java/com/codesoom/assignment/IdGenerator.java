package com.codesoom.assignment;

public class IdGenerator {
    private static Long count = 0L;
    private IdGenerator() {
    }

    public static Long generate() {
        return count++;
    }
}
