package com.codesoom.assignment.models.response;

import com.codesoom.assignment.models.HttpStatus;

public class ResponseNotFound extends Response {
    public ResponseNotFound(String content) {
        super(HttpStatus.NOT_FOUND.value(), content);
    }
}
