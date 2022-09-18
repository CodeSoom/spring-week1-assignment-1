package com.codesoom.http;

import com.codesoom.exception.MethodNotExistException;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequest {
    private final HttpMethod method;
    private final String path;
    private final String body;

    public HttpRequest(HttpMethod method, String path, String body) {
        this.method = method;
        this.path = path;
        this.body = body;
    }

    public HttpRequest(HttpExchange exchange) {
        method = HttpMethod.of(exchange.getRequestMethod());
        path = exchange.getRequestURI().getPath();
        InputStream in = exchange.getRequestBody();
        body = new BufferedReader(new InputStreamReader(in))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public static boolean isValidRequest(HttpExchange exchange) {
        try {
            HttpMethod.of(exchange.getRequestMethod());
            return true;
        } catch (MethodNotExistException e) {
            return false;
        }
    }

    public Long getLongFromPathParameter(int idx) {
        String[] splitPath = path.split("/");
        if (idx < splitPath.length) {
            try {
                return Long.parseLong(splitPath[idx]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("id가 숫자가 아닙니다.");
            }
        }
        throw new IllegalArgumentException("id가 존재하지 않습니다.");
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
