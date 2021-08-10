package com.codesoom.assignment.todolist.exceptions;

public class NotFoundSupportedResolverException extends RuntimeException{

    public static final String DEFAULT_MESSAGE = "유효한 파라미터 리졸버를 찾지 못했습니다.";

    public NotFoundSupportedResolverException() {
        super(DEFAULT_MESSAGE);
    }

    public NotFoundSupportedResolverException(String message) {
        super(message);
    }
}
