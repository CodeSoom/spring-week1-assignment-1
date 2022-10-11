package com.codesoom.assignment;

public enum HttpMethod {
    GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE");

    final String name;

    HttpMethod(String name) {
        this.name = name;
    }

}
