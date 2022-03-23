package com.codesoom.assignment.enums;

public enum HttpStatusCode {

    OK(200, "정상적으로 처리되었습니다."),
    CREATED(201, "정상적으로 생성되었습니다."),

    BAD_REQUEST(400, "잘못된 요청입니다."),
    NOT_FOUND(404, "결과를 찾을 수 없습니다."),

    /**
     * CUSTOM ERRORS
     * */
    WRONG_TASK_ID(600, "잘못된 task id 입니다."),
    BAD_CONTENT_FORMAT(601, "잘못된 콘텐츠 형식입니다.");

    private final int code;

    private final String message;

    HttpStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
