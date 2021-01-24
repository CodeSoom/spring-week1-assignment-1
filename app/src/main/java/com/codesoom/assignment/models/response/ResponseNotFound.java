package com.codesoom.assignment.models.response;

import com.codesoom.assignment.models.HttpStatus;

public class ResponseNotFound extends Response {
    private static final int statusCode = HttpStatus.NOT_FOUND.value();

    public ResponseNotFound(String content) {
        super(statusCode, content);
    }
}
