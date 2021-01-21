package com.codesoom.assignment.web.models;

public class HttpResponse {
    private final int code;
    private final String content;

    public HttpResponse(int code) {
        this("", code);
    }

    public HttpResponse(String content, int code) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }

}
