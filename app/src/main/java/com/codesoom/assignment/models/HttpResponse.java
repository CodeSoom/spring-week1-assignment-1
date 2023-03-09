package com.codesoom.assignment.models;

public class HttpResponse {
    private String content;
    private int httpCode;

    public static HttpResponse failResponse() {
        return new HttpResponse("", 404);
    }

    public HttpResponse(String content, int httpCode) {
        this.content = content;
        this.httpCode = httpCode;
    }

    public String getContent() {
        return content;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void updateHttpCode(int code) {
        this.httpCode = code;
    }

    public void updateContent(String code) {
        this.content = content;
    }
}
