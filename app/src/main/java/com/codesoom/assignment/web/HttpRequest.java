package com.codesoom.assignment.web;

public class HttpRequest {
    String path;
    String requestBody;

    public HttpRequest(String path, String requestBody) {
        this.path = path;
        this.requestBody = requestBody;
    }

    Long getTaskId() {
        String resourceId = path.split("/")[2];
        return (long) Integer.parseInt(resourceId);
    }
}
