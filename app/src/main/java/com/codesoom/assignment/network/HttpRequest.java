package com.codesoom.assignment.network;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Http 요청에 관련된 정보
 */
public class HttpRequest {
    private HttpMethod method;
    private String path;
    private String body;

    public HttpRequest(HttpExchange exchange) {
        this.method = HttpMethod.convert(exchange.getRequestMethod());
        this.path = exchange.getRequestURI().getPath();
        this.body = getRequestBody(exchange).orElse("");
    }

    public boolean isNotValid() {
        return method == null || path == null;
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

    private Optional<String> getRequestBody(HttpExchange exchange) {
        final InputStream inputStream = exchange.getRequestBody();
        if (inputStream == null) {
            return Optional.empty();
        }

        final String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        return Optional.ofNullable(body);
    }

}
