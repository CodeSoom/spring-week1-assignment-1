package com.codesoom.assignment.models;

import com.codesoom.assignment.HttpStatusCode;

public class HttpResponse {
    String content;
    HttpStatusCode httpStatusCode;

    public HttpResponse(String content, HttpStatusCode httpStatusCode) {
        this.content = content;
        this.httpStatusCode = httpStatusCode;
    }
}
