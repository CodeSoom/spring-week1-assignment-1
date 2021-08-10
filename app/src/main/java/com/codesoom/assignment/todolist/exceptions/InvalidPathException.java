package com.codesoom.assignment.todolist.exceptions;

public class InvalidPathException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "유효하지 않은 경로입니다.";

    public InvalidPathException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidPathException(String message) {
        super(message);
    }
}
