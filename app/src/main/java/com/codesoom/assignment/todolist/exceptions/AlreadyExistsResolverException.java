package com.codesoom.assignment.todolist.exceptions;

public class AlreadyExistsResolverException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "이미 존재하는 파라미터 리졸버입니다.";

    public AlreadyExistsResolverException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyExistsResolverException(String message) {
        super(message);
    }
}
