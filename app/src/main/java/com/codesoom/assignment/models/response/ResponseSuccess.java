package com.codesoom.assignment.models.response;

import com.codesoom.assignment.models.HttpStatus;

public class ResponseSuccess extends Response {
    private static final int statusCode = HttpStatus.OK.value();

    public ResponseSuccess(String content) {
        super(statusCode, content);
    }
}
