package com.codesoom.assignment.models;

public enum MethodType {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String methodName;
    MethodType(String methodName) {
        this.methodName = methodName;
    }

    public String toString() {
        return methodName;
    }
}
