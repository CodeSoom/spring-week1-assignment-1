package com.codesoom.http;

import com.codesoom.controller.HttpMethod;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HttpRequest {
    // todo 필드  validate 추가 예정
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

    public Long getLongFromPathParameter(int idx) {
        // todo 아래의 경우 예외처리하기
        // /tasks/1/ -> 1이 return
        // /tasks/ -> 예외
        // /tasks  -> 예외
        String[] splitPath = path.split("/");
        if (idx < splitPath.length) {
            return Long.parseLong(splitPath[idx]);
        }
        return null;
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
