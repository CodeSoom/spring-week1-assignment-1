package com.codesoom.assignment.models;

public class HttpResponse {

    public static final int HTTP_STATUS_CODE_OK = 200;
    public static final int HTTP_STATUS_CODE_CREATED = 201;
    public static final int HTTP_STATUS_CODE_NO_CONTENT = 204;
    public static final int HTTP_STATUS_CODE_NOT_FOUND = 404;
    public static final int HTTP_STATUS_CODE_METHOD_NOT_ALLOWED = 405;

    private int httpStatusCode;

    private String content = "";

    public HttpResponse(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public HttpResponse(int httpStatusCode, String content) {
        this.httpStatusCode = httpStatusCode;
        this.content = content;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getContent() {
        return content;
    }

}
