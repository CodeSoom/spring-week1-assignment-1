package com.codesoom.assignment;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private final String method;

    HttpMethod(String method){
        this.method = method;
    }

    public String getMethod(){
        return method;
    }
}
