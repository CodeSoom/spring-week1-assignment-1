package com.codesoom.assignment;

/**
 * http methods
 */
//public class HttpMethod {
//    public static final String GET = "GET";
//    public static final String HEAD = "HEAD";
//    public static final String POST = "POST";
//    public static final String PUT = "PUT";
//    public static final String PATCH = "PATCH";
//    public static final String DELETE = "DELETE";
//}

public enum HttpMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE");

    private String rawString;

    HttpMethod(String rawString) {
        this.rawString = rawString;
    }

    public String getRawString() {
        return rawString;
    }
}
