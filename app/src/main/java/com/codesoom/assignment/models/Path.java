package com.codesoom.assignment.models;

import com.codesoom.assignment.exceptions.IllegalHttpRequestPathException;
import com.sun.net.httpserver.HttpExchange;

import java.net.URI;

public class Path {

    private final String path;
    private final Long id;

    public Path(HttpExchange exchange) throws IllegalHttpRequestPathException {
        final URI uri = exchange.getRequestURI();
        this.path = uri.getPath();
        if (!isValid(path)) {
            throw new IllegalHttpRequestPathException("잘못된 경로입니다. .../tasks/{Number} 형식으로 입력해주세요.");
        }

        id = resolveId(this.path);
    }

    private Long resolveId(String path) {
        String[] pathArr = path.split("/");
        if (pathArr.length != 3) {
            return null;
        }

        return Long.valueOf(pathArr[2]);
    }

    public boolean isValid(String path) {
        if (path == null || !path.contains("/")) {
            return false;
        }

        final String[] pathArr = path.split("/");
        if (!(pathArr.length == 2 || pathArr.length == 3)) {
            return false;
        }

        final String resourceName = pathArr[1];
        if (!resourceName.equals("tasks")) {
            return false;
        }

        if (pathArr.length == 2) {
            return true;
        }

        final String idPart = pathArr[2];
        try {
            Long.valueOf(idPart);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public Long getId() {
        return id;
    }
}
