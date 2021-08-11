package com.codesoom.assignment.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class RequestInfo {
    private String method;
    private String path;
    private String body;

    public RequestInfo(HttpExchange exchange) throws UnsupportedEncodingException {
        this.method = exchange.getRequestMethod();

        URI uri = exchange.getRequestURI();
        this.path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        this.body = new BufferedReader(new InputStreamReader(inputStream, "utf-8"))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public RequestInfo(String method, String path, String body) {
        this.method = method;
        this.path = path;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
