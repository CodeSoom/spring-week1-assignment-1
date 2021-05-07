package com.codesoom.assignment.exception;

import java.util.NoSuchElementException;

public class NotFoundTaskIdException extends NoSuchElementException {
    private static final String message = "Not Found Task Id";
    public NotFoundTaskIdException() {
        super(message);
    }
}
