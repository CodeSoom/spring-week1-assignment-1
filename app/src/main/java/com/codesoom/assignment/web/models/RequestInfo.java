package com.codesoom.assignment.web.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class RequestInfo {
    private String method;
    private String path;
    private String body;

    public RequestInfo(HttpExchange exchange) {
        this.method = exchange.getRequestMethod();
        this.path = exchange.getRequestURI().getPath();
        InputStream bodyInputStream = exchange.getRequestBody();
        this.body = new BufferedReader(new InputStreamReader(bodyInputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "RequestInfo{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}