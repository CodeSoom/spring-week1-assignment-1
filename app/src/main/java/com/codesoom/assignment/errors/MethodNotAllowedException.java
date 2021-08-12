package com.codesoom.assignment.errors;

public class MethodNotAllowedException extends RuntimeException {

    private static final String MESSAGE = "허용되지 않은 메서드 입니다.";

    public MethodNotAllowedException() {
        super(MESSAGE);
    }
}
