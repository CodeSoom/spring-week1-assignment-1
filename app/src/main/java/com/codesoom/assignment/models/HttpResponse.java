package com.codesoom.assignment.models;

public class HttpResponse {

    private HttpStatus httpStatus;

    private String content = "";

    public HttpResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpResponse(HttpStatus httpStatus, String content) {
        this.httpStatus = httpStatus;
        this.content = content;
    }

    public int getHttpStatusCode() {
        return httpStatus.getCode();
    }

    public String getContent() {
        return content;
    }

}
