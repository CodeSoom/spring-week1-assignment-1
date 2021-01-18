package com.codesoom.assignment;

public class IdGenerator {
    private static Long COUNT = 1L;
    private IdGenerator() {
    }

    public static Long generate() {
        return COUNT++;
    }
}
