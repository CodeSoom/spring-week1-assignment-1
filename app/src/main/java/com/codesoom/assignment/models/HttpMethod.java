package com.codesoom.assignment.models;

/**
 * HTTP 프로토콜의 메소드를 정의합니다.
 * @link https://datatracker.ietf.org/doc/html/rfc2616#section-9
 */
public enum HttpMethod {
    GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE");

    private String httpMethod;

    HttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHttpMethod() {
        return httpMethod;
    }
}
