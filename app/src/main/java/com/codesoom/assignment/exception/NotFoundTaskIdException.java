package com.codesoom.assignment.exception;

import java.util.NoSuchElementException;

public class NotFoundTaskIdException extends NoSuchElementException {
    private static final String message = "요청하신 Task ID를 찾을 수 없습니다.";
    public NotFoundTaskIdException() {
        super(message);
    }
}
