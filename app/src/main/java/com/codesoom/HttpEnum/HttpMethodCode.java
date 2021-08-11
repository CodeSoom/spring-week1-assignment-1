package com.codesoom.HttpEnum;

public enum HttpMethodCode {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private String Status;

    HttpMethodCode(String status) {
        this.Status = status;
    }

    public String getStatus() {
        return this.Status;
    }


}
