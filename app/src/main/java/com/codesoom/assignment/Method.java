package com.codesoom.assignment;

public enum Method {
    GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE");

    final String name;

    Method(String name) {
        this.name = name;
    }

}
