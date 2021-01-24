package com.codesoom.assignment.web;

public class HttpRequest {
    String path;
    String method;
    String requestBody;

    public HttpRequest(String method, String path, String requestBody) {
        this.method = method;
        this.path = path;
        this.requestBody = requestBody;
    }

    Long getTaskId() {
        String resourceId = path.split("/")[2];
        return (long) Integer.parseInt(resourceId);
    }
}
