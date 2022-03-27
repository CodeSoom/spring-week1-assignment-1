package com.codesoom.assignment.task.exception;

public final class TaskNotFoundException extends IllegalStateException {

    private static final String MESSAGE = "Not Found Task";

    public TaskNotFoundException() {
        super(MESSAGE);
    }
}
