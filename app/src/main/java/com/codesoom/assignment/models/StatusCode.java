package com.codesoom.assignment.models;

/**
 * Http 상태코드를 정의합니다.
 * @link https://datatracker.ietf.org/doc/html/rfc7231#page-47
 */
public enum StatusCode {
    OK(200), Created(201), NO_CONTENT(204), NOT_FOUND(404);

    private int statusCode;

    StatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getValue() {
        return statusCode;
    }
}
