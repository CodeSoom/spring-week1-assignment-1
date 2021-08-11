package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;
import java.net.URI;
import java.util.Arrays;
import java.util.regex.Pattern;

public class HttpRequest {

    private final String path;
    private final String method;

    public HttpRequest(HttpExchange httpExchange) {
        URI requestURI = httpExchange.getRequestURI();

        this.path = requestURI.getPath();
        this.method = httpExchange.getRequestMethod();
    }

    public long getTaskIdFromPath() {
        return Arrays.stream(path.split("/"))
            .skip(2)
            .mapToLong(Long::parseLong)
            .findFirst()
            .getAsLong();
    }

    public boolean isReadAll() {
        return "GET".equals(method) && "/tasks".equals(path);
    }

    public boolean isReadOne() {
        return "GET".equals(method) && hasTaskId();
    }

    public boolean isCreateOne() {
        return "POST".equals(method) && "/tasks".equals(path);
    }

    public boolean isUpdateOne() {
        return ("PUT".equals(method) || "PATCH".equals(method)) && path.startsWith("/tasks")
            && hasTaskId();
    }

    public boolean isDeleteOne() {
        return "DELETE".equals(method) && path.startsWith("/tasks") && hasTaskId();
    }

    private boolean hasTaskId() {
        return Pattern.matches("/tasks/[0-9]+$", path);
    }

    @Override
    public String toString() {
        return String.format("HttpRequest {method=%s, path=%s} ", method, path);
    }

}