package com.codesoom.assignment.enums;

/**
 * HTTP Method를 관리한다 <a href="https://datatracker.ietf.org/doc/html/rfc7231#section-4.3">RFC7231</a>
 *
 * @see HttpMethod#equals(String) HTTP Method 비교 함수
 */
public enum HttpMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETED;
    
    public boolean equals(String method){
        return this.name().equals(method);
    }
}
