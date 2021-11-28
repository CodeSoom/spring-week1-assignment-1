package com.codesoom.assignment.models;

public class Response {
    int statusCode;
    String content;

    public Response() {
        this.statusCode = 500;
        this.content = "";
    }

    public Response(int statusCode, String content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() {
        return content;
    }
}
