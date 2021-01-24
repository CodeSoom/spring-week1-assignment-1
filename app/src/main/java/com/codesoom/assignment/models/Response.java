package com.codesoom.assignment.models;

import com.codesoom.assignment.HttpStatusCode;

public class Response {

    private String content;
    private HttpStatusCode httpStatusCode;

    public Response(String content, HttpStatusCode httpStatusCode) {
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
