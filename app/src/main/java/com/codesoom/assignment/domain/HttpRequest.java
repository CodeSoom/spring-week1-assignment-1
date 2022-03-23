package com.codesoom.assignment.domain;


import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequest {
    private final String method;
    private final String body;
    private final String path;

    public HttpRequest(String method, String body, String path) {
        this.method = method;
        this.body = body;
        this.path = path;
    }

    public HttpRequest(HttpExchange httpExchange) {
        this.method = httpExchange.getRequestMethod();
        InputStream httpBodyInputStream = httpExchange.getRequestBody();
        this.body = parseInputStream(httpBodyInputStream);
        this.path = httpExchange.getRequestURI().getPath();
    }

    /**
     * InputStream 을 파싱하여 문자열로 반환한다.
     * @param inputStream 파싱될 InputStream
     * @return parsedString 파싱된 InputStream
     */
    private String parseInputStream(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public String getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public String getPath() {
        return path;
    }
}
