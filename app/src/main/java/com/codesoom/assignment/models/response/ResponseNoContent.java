package com.codesoom.assignment.models.response;

import com.codesoom.assignment.models.HttpStatus;

public class ResponseNoContent extends Response {
    public ResponseNoContent(String content) {
        super(HttpStatus.NO_CONTENT.value(), content);
    }
}
