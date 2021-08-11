package com.codesoom.assignment.errors;

public class TaskIdNotFoundException extends RuntimeException{
    public static final String MESSAGE = "Can't find task from your id.";

    public TaskIdNotFoundException() {
        super(MESSAGE);
    }
}
