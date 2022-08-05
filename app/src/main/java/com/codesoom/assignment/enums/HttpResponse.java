package com.codesoom.assignment.enums;

/**
 * HTTP Response Status Code를 관리한다 <a href="https://datatracker.ietf.org/doc/html/rfc9110#section-15">RFC9110</a>
 *
 * @see HttpResponse#getCode() Response Status Code를 반환한다
 */
public enum HttpResponse {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    NO_CONTENT(204),
    NOT_FOUND(404);

    private final int code;

    HttpResponse(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
