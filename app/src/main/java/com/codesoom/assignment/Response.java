package com.codesoom.assignment;

public class Response {
    private final int status;
    private final String body;

    public Response(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }
}
