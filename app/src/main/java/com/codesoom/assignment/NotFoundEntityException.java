package com.codesoom.assignment;

public class NotFoundEntityException extends RuntimeException{
    public static final String DEFAULT_MESSAGE = "요소를 찾을 수 없습니다.";

    public NotFoundEntityException() {
        super(DEFAULT_MESSAGE);
    }

    public NotFoundEntityException(String message) {
        super(message);
    }
}
