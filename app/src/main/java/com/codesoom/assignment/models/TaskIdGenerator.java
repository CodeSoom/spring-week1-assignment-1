package com.codesoom.assignment.models;

public class TaskIdGenerator {
    private static Long sequence = 0L;

    public static Long generateSequence() {
        return sequence++;
    }

    public static Long getLastSequence() {
        return sequence - 1;
    }
}
