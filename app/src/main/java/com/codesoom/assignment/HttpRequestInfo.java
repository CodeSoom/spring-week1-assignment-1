package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequestInfo {

    private String method;
    private String path;
    private String body;

    public HttpRequestInfo(HttpExchange exchange) {
        this.method = exchange.getRequestMethod(); // request로 어떤 메서드가 들어오는지 확인
        this.path = exchange.getRequestURI().getPath(); // path로 어떤 값이 들어오는지 확인
        InputStream inputStream = exchange.getRequestBody();
        this.body = new BufferedReader(new InputStreamReader(inputStream))
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
        return "method=" + method + ", path=" + path + ", body=" + body;
    }

}
