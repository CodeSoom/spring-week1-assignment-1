package com.codesoom.assignment.domain.http;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpResponse {
    private final String content;
    private final int statusCode;
    private static final Charset DEFAULT_ENCODING = UTF_8;

    public HttpResponse(String content, int statusCode) {
        this.content = content;
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public int getContentLength() {
        return this.content.getBytes(DEFAULT_ENCODING).length;
    }

    public byte[] getContentAsByte() {
        return this.content.getBytes(DEFAULT_ENCODING);
    }

    public int getStatusCode() {
        return statusCode;
    }
}
