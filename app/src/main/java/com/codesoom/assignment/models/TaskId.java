package com.codesoom.assignment.models;

public class TaskId {
    private static Long id = 0L;

    private TaskId() {
    }

    public static synchronized Long getNewId() {
        increaseId();
        return id;
    }

    private static void increaseId() {
        ++id;
    }
}
