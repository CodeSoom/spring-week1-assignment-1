package com.codesoom.assignment;

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

    public boolean isMatchMethod(String method) {
        return method.equals(this.method);
    }

    public boolean isMatchPath(String path) {
        return path.equals(this.path);
    }

    public boolean isUpdateMethod() {
        return "PUT".equals(method) || "PATCH".equals(method);
    }

    public long getTaskIdFromPath() {
        return Arrays.stream(path.split("/"))
            .skip(2)
            .mapToLong(Long::parseLong)
            .findFirst()
            .getAsLong();
    }

    public boolean hasTaskId() {
        return Pattern.matches("/tasks/[0-9]+$", path);
    }

    public boolean pathStartsWith(String path) {
        return this.path.startsWith(path);
    }

    @Override
    public String toString() {
        return String.format("HttpRequest {method=%s, path=%s} ", method, path);
    }

}
