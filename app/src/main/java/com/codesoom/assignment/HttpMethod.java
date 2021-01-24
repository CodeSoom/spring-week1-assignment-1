package com.codesoom.assignment;

public enum HttpMethod {

    GET, POST, PUT, PATCH, DELETE, HEAD;

    public static boolean isProperMethod(HttpMethod method) {
        return method.equals(HttpMethod.GET) ||
                method.equals(HttpMethod.POST) ||
                method.equals(HttpMethod.PUT) ||
                method.equals(HttpMethod.PATCH) ||
                method.equals(HttpMethod.DELETE);
    }
}
