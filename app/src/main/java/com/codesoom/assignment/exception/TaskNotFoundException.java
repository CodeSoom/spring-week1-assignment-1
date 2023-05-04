package com.codesoom.assignment.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(final long id) {
        super(String.format("Task ID : %d not found", id));
    }

}
