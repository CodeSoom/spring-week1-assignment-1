package com.codesoom.assignment.models;

/**
 * HTTP 메서드를 정의합니다.
 * https://datatracker.ietf.org/doc/html/rfc7231#section-4.3
 */
public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE;

    public static HttpMethod setHttpMethod(String method) {
        switch (method) {
            case "GET":
                return HttpMethod.GET;
            case "POST":
                return HttpMethod.POST;
            case "PUT":
                return HttpMethod.PUT;
            case "PATCH":
                return HttpMethod.PATCH;
            case "DELETE":
                return HttpMethod.DELETE;
            default:
                throw new AssertionError("정의되지 않은 HTTP 메서드입니다.");
        }
    }
}
