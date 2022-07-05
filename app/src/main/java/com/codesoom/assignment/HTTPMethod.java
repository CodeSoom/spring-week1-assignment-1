package com.codesoom.assignment;

public enum HTTPMethod {
    GET, POST, PUT, PATCH, DELETE;

    public static HTTPMethod convert(String method) {
        try {
            return HTTPMethod.valueOf(method);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }
}
