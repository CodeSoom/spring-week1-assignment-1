package com.codesoom.assignment.network;

/**
 * Http Response Code를 enum으로 추상화
 */
public enum HttpResponseCode {
    OK(200),
    Created(201),
    NoContent(204),
    BadRequest(400),
    NotFound(404),
    InternalServerError(500);

    /**
     * http code raw value
     */
    private final int rawValue;

    /**
     * @param rawValue http code를 raw value로 전달합니다
     */
    HttpResponseCode(int rawValue) {
        this.rawValue = rawValue;
    }

    /**
     * @return http code를 raw value로 전달합니다
     */
    public int getRawValue() {
        return rawValue;
    }
}
