package com.codesoom.assignment.models;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Collectors;

public abstract class HttpRequest {

    private TasksHttpRequestMethod method;

    private String path;

    private String body;

    public HttpRequest(String method, URI uri, InputStream inputStream) {
        this.method = TasksHttpRequestMethod.valueOf(method);
        this.path = uri.getPath();
        this.body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public TasksHttpRequestMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }

    public abstract boolean isValidPath();

    public abstract boolean isValidMethod();

}
