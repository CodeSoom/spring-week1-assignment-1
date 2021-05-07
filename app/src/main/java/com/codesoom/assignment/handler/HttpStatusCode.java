package com.codesoom.assignment.handler;

enum HttpStatusCode {
    OK(200), Created(201), NoContent(204), BadRequest(400), NotFoundError(404);

    final private int code;

    public int getCode() {
        return code;
    }

    private HttpStatusCode(int code) {
        this.code = code;
    }
}
