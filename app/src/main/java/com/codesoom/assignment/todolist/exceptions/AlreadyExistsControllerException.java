package com.codesoom.assignment.todolist.exceptions;

public class AlreadyExistsControllerException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "이미 존재하는 컨트롤러입니다.";

    public AlreadyExistsControllerException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyExistsControllerException(String message) {
        super(message);
    }
}
