package com.codesoom.assignment.domain.http;


import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HttpRequest {
    private final String method;
    private final String body;
    private final String path;
    private final List<String> pathVariables = new ArrayList<>();

    public HttpRequest(String method, String body, String path) {
        this.method = method;
        this.body = body;
        this.path = path;
    }

    public HttpRequest(HttpExchange httpExchange) {
        this.method = getMethod(httpExchange);
        this.body = getBody(httpExchange);
        this.path = getPath(httpExchange);
        getPathVariable();
    }

    private String getPath(HttpExchange httpExchange) {
        return httpExchange.getRequestURI().getPath();
    }

    private String getMethod(HttpExchange httpExchange) {
        return httpExchange.getRequestMethod();
    }

    private String getBody(HttpExchange httpExchange) {
        InputStream httpBodyInputStream = httpExchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(httpBodyInputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private void getPathVariable() {
        String[] pathSegments = this.path.split("/");

        pathVariables.addAll(
                Arrays.asList(pathSegments)
                        .subList(2, pathSegments.length));
    }

    public boolean hasPathVariable() {
        return pathVariables.size() >= 1;
    }

    public String[] getPathVariables() {
        // 함부로 List 를 참조해서 변환할 수 없게 한다.
        return pathVariables.toArray(new String[]{});
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
