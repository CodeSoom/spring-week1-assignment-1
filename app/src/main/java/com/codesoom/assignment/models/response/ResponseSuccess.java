package com.codesoom.assignment.models.response;

import com.codesoom.assignment.models.HttpStatus;

public class ResponseSuccess extends Response {
    public ResponseSuccess(String content) {
        super(HttpStatus.OK.value(), content);
    }
}
