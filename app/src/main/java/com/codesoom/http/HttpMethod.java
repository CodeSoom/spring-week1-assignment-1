package com.codesoom.http;

import com.codesoom.exception.MethodNotExistException;

public enum HttpMethod {
    GET, POST, PUT, DELETE;

    public boolean isPost() {
        return this == POST;
    }

    public boolean isGet() {
        return this == GET;
    }

    public boolean isPut() {
        return this == PUT;
    }

    public boolean isDelete() {
        return this == DELETE;
    }

    public static HttpMethod of(String method) {
        try {
            return HttpMethod.valueOf(method);
        } catch (IllegalArgumentException e) {
            throw new MethodNotExistException();
        }
    }
}
