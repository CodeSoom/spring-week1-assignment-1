package com.codesoom.controller;

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
}
