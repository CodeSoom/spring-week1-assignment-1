package com.codesoom.assignment;

/**
 * Http 상태코드를 정의합니다.
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc2616">RFC 2616</a>
 */
public enum HttpStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
