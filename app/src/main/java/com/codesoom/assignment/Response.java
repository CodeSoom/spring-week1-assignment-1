package com.codesoom.assignment;

public class Response {
    private final int code;
    private final String body;

    public Response(HttpStatus status) {
        this(status, "");
    }

    public Response(HttpStatus status, String body) {
        this.code = status.getCode();
        this.body = body;
    }

    public int getStatus() {
        return code;
    }

    public String getBody() {
        return body;
    }
}
