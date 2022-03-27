package com.codesoom.assignment.domain.http;

/**
 * https://datatracker.ietf.org/doc/html/rfc7231#section-6.1
 */
public interface HttpStatus {
    int SUCCESS = 200;
    int CREATED = 201;
    int BAD_REQUEST = 400;
    int NOT_FOUND = 404;
    int METHOD_NOT_ALLOWED = 405;
    int INTERNAL_SERVER_ERROR = 500;
}
