package com.codesoom.assignment.network;

public class HttpRequest {
    private String path;
    private String body;

    public HttpRequest(String path, String body) {
        this.path = path;
        this.body = body;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }
}
