package com.codesoom.assignment.models;

import com.codesoom.assignment.exceptions.IllegalHttpRequestPathException;
import com.codesoom.assignment.utils.HttpRequestValidator;
import com.codesoom.assignment.utils.StringValidator;
import com.sun.net.httpserver.HttpExchange;

import java.net.URI;

public class Path {

    private final String path;
    private final Long id;

    public Path(HttpExchange exchange) throws IllegalHttpRequestPathException {
        final URI uri = exchange.getRequestURI();
        this.path = uri.getPath();
        if (!HttpRequestValidator.isPathValid(path)) {
            throw new IllegalHttpRequestPathException("잘못된 경로입니다. .../tasks/{Number} 형식으로 입력해주세요.");
        }

        id = resolveId(this.path);
    }

    private Long resolveId(String path) {
        final String[] pathArr = path.split("/");
        if (pathArr.length != 3) {
            return null;
        }

        return Long.valueOf(pathArr[2]);
    }

    public Long getId() {
        return id;
    }
}
