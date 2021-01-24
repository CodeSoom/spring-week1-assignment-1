package com.codesoom.assignment.models.response;

import com.codesoom.assignment.models.HttpStatus;

public class ResponseNoContent extends Response {
    private static final int statusCode = HttpStatus.NO_CONTENT.value();

    public ResponseNoContent(String content) {
        super(statusCode, content);
    }
}
