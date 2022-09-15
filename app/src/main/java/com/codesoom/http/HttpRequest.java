package com.codesoom.http;

import com.codesoom.controller.HttpMethod;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final String body;

    public HttpRequest(HttpExchange exchange) {
        method = HttpMethod.valueOf(exchange.getRequestMethod());
        path = exchange.getRequestURI().getPath();
        InputStream in = exchange.getRequestBody();
        body = new BufferedReader(new InputStreamReader(in))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }
}
