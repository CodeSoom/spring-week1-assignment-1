package com.codesoom.assignment.web.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequest {
    private final HttpRequestMethod method;
    private final String path;
    private final String body;

    public HttpRequest(HttpExchange exchange) {
        this.method = HttpRequestMethod.fromString(exchange.getRequestMethod());
        this.path = exchange.getRequestURI().getPath();
        InputStream bodyInputStream = exchange.getRequestBody();
        this.body = new BufferedReader(new InputStreamReader(bodyInputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public HttpRequestMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }

    public boolean isServerHealthCheck() {
        return path.equals("/") && method.equals(HttpRequestMethod.HEAD);
    }


    @Override
    public String toString() {
        return String.format("RequestInfo { method=%s, path=%s, body=%s }", method, path, body);
    }

}
