package com.codesoom.assignment.network;

import java.util.Objects;

public class HttpRouterKey {
    private HttpMethod method;
    private String pathRegex;
    private int hashCode;

    public HttpRouterKey(HttpMethod method, String path) {
        this.method = method;
        this.pathRegex = path;
        this.hashCode = Objects.hash(method, path);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        HttpRouterKey that = (HttpRouterKey) obj;
        return method == that.method && pathRegex == that.pathRegex;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public String getPathRegex() {
        return pathRegex;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public boolean matchesPath(String path) {
        if (path == null || this.pathRegex == null) {
            return false;
        }
        return path.matches(pathRegex);
    }

    public boolean equalsMethod(HttpMethod method) {
        if (method == null || this.method == null) {
            return false;
        }
        return method.equals(this.method);
    }
}
