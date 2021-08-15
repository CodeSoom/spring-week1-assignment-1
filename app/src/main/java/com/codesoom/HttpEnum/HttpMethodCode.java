package com.codesoom.HttpEnum;

/**
 * HTTP 프로토콜 메소드를 정의합니다.
 * @link https://datatracker.ietf.org/doc/html/rfc2616#section-9
 */
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
