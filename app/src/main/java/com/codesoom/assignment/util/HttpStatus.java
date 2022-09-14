package com.codesoom.assignment.util;

public enum HttpStatus {
    HTTP_OK(200, "Created"),

    HTTP_CREATED(201, "Accepted"),

    HTTP_ACCEPTED(202, "Non-Authoritative Information"),

    HTTP_NOT_AUTHORITATIVE(203, "No Content"),

    HTTP_NO_CONTENT(204, "Reset Content"),

    HTTP_RESET(205, "Partial Content"),

    HTTP_PARTIAL(206, "Multiple Choices"),

    HTTP_MULT_CHOICE(300, "Moved Permanently"),

    HTTP_MOVED_PERM(301, "Temporary Redirect"),

    HTTP_MOVED_TEMP(302, "See Other"),

    HTTP_SEE_OTHER(303, "Not Modified"),

    HTTP_NOT_MODIFIED(304, "Use Proxy"),

    HTTP_USE_PROXY(305, "Bad Request"),

    HTTP_BAD_REQUEST(400, "Unauthorized"),

    HTTP_UNAUTHORIZED(401, "Payment Required"),

    HTTP_PAYMENT_REQUIRED(402, "Forbidden"),

    HTTP_FORBIDDEN(403, "Not Found"),

    HTTP_NOT_FOUND(404, "Method Not Allowed"),

    HTTP_BAD_METHOD(405, "Not Acceptable"),

    HTTP_NOT_ACCEPTABLE(406, "Proxy Authentication Required"),

    HTTP_PROXY_AUTH(407, "Request Time-Out"),

    HTTP_CLIENT_TIMEOUT(408, "Conflict"),

    HTTP_CONFLICT(409, "Gone"),

    HTTP_GONE(410, "Length Required"),

    HTTP_LENGTH_REQUIRED(411, "Precondition Failed"),

    HTTP_PRECON_FAILED(412, "Request Entity Too Large"),

    HTTP_ENTITY_TOO_LARGE(413, "Request-URI Too Large"),

    HTTP_REQ_TOO_LONG(414, "Unsupported Media Type"),

    HTTP_UNSUPPORTED_TYPE(415, "Internal Server Error"),

    HTTP_INTERNAL_ERROR(500, "Not Implemented"),

    HTTP_NOT_IMPLEMENTED(501, "Bad Gateway"),

    HTTP_BAD_GATEWAY(502, "Service Unavailable"),

    HTTP_UNAVAILABLE(503, "Gateway Timeout"),

    HTTP_GATEWAY_TIMEOUT(504, "HTTP Version Not Supported"),

    HTTP_VERSION(505, "HTTP Version Not Supported");

    private final int statusCode;
    private final String description;

    HttpStatus(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return description;
    }
}
