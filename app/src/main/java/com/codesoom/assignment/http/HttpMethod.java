package com.codesoom.assignment.http;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private String httpsStatus;

    HttpMethod(String httpsStatus) {
        this.httpsStatus = httpsStatus;
    }

    public String getHttpsStatus() {
        return httpsStatus;
    }

}
