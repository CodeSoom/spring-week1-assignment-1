package com.codesoom.assignment;

public enum HttpMethod {

    GET, POST, PUT, DELETE;

    public static boolean isProperMethod(HttpMethod method) {
        return method.equals(HttpMethod.GET) ||
                method.equals(HttpMethod.POST) ||
                method.equals(HttpMethod.PUT) ||
                method.equals(HttpMethod.DELETE);
    }
}
