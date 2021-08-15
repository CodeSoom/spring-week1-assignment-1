package com.codesoom.HttpEnum;

/**
 * HTTP 상태코드를 정의합니다.
 * @link https://datatracker.ietf.org/doc/html/rfc7231#section-6
 */
public enum HttpStatusCode {

    OK(200),
    NOT_FOUND(404),
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400);

    private int status;

    HttpStatusCode(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }


}
