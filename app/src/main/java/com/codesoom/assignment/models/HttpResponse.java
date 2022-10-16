package com.codesoom.assignment.models;

import com.codesoom.assignment.HttpStatusCode;

public class HttpResponse {
    private final String content;
    private final HttpStatusCode httpStatusCode;

    public HttpResponse(String content, HttpStatusCode httpStatusCode) {
        this.content = content;
        this.httpStatusCode = httpStatusCode;
    }

    public String getContent() {
        return content;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}
