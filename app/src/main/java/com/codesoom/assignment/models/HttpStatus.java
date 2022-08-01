package com.codesoom.assignment.models;

/**
 * Http 응답에 사용할 상태 코드
 */
public enum HttpStatus {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    NOT_FOUND(404);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    /**
     * Http 응답 헤더에 코드 값을 리턴합니다.
     *
     * @return Status의 코드 값 리턴
     */
    public int getCode() {
        return code;
    }
}
