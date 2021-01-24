package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequest {

    private String method;
    private String path;
    private String body;
    private Long id;

    public HttpRequest(HttpExchange exchange) {
        this.method = exchange.getRequestMethod(); // request로 어떤 메서드가 들어오는지 확인
        this.path = exchange.getRequestURI().getPath(); // path로 어떤 값이 들어오는지 확인
        InputStream inputStream = exchange.getRequestBody();
        this.body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        this.id = getIdFromPath(path);
    }

    public String hasMethod() {
        return method;
    }

    public String hasPath() {
        return path;
    }

    public String hasBody() {
        return body;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("method=%s, path=%s, body=%body", method, path, body);
    }

    private Long getIdFromPath(String path) {
        String idInPath = "";
        if (path.contains("/tasks/")) {
            idInPath = path.replace("/tasks/", "");
            return Long.parseLong(idInPath);
        }
        return null;
    }
}
