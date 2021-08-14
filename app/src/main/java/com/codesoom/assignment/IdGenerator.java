package com.codesoom.assignment;

public final class IdGenerator {
    private static Long CURRENT_ID = 1L;

    public static Long generateId() {
        final Long id = CURRENT_ID;
        ++CURRENT_ID;
        return id;
    }

    public static void undoIdGeneration() {
        --CURRENT_ID;
    }
}
