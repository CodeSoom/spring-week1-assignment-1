package com.codesoom.assignment.network;

/**
 * HTTP 프로토콜의 Method를 enum으로 추상화
 */
public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE;

    /**
     * String으로 method를 enum method로 변환
     * @param method method raw value
     * @return HttpMethod enum 으로 변환된 값. 변경 안되면 null로 반환됨
     */
    public static HttpMethod convert(String method) {
        try {
            return HttpMethod.valueOf(method);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }
}
