package com.codesoom.assignment.models.response;

import com.codesoom.assignment.models.HttpStatus;

public class ResponseCreated extends Response {
    public ResponseCreated(String content) {
        super(HttpStatus.CREATED.value(), content);
    }
}
