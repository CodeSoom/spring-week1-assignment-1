package com.codesoom.assignment.network;

public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE;

    public static HttpMethod convert(String method) {
        try {
            return HttpMethod.valueOf(method);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }
}
