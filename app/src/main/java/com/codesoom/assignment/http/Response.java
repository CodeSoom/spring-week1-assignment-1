package com.codesoom.assignment.http;

public class Response {
    private int statusCode;
    private Object body;

    public Response(int statusCode, Object body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public static Response of(int statusCode, Object body) {
        return new Response(statusCode, body);
    }

    public static Response from(int statusCode) {
        return new Response(statusCode, null);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Object getBody() {
        return body;
    }
}
