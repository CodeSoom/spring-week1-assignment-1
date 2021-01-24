package com.codesoom.assignment.models.response;

import com.codesoom.assignment.models.HttpStatus;

public class ResponseCreated extends Response {
    private static final int statusCode = HttpStatus.CREATED.value();

    public ResponseCreated(String content) {
        super(statusCode, content);
    }
}
