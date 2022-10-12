package com.codesoom.assignment;

public enum HttpStatusCode {
    OK(200), NO_CONTENT(204), BAD_REQUEST(400), NOT_FOUND(404);

    final int code;

    HttpStatusCode(int code) {
        this.code = code;
    }
}
