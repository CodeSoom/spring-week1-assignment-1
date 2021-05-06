package com.codesoom.assignment.handler;

enum Code {
    OK(200), Created(201), NoContent(204), BadRequest(400);

    final private int code;

    public int getCode() {
        return code;
    }

    private Code(int code) {
        this.code = code;
    }
}
