package com.codesoom.assignment.errors;

public class NotAllowedMethodException extends RuntimeException {

    private static final String MESSAGE = "허용되지 않은 메서드입니다: ";

    public NotAllowedMethodException(String method) {
        super(MESSAGE + method);
    }
}
