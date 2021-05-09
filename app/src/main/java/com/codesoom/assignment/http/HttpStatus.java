package com.codesoom.assignment.http;

/**
 * 상태 코드를 표현합니다.
 *
 * <ol>
 *     <li>1xx (Informational): 요청이 수신되었고 프로세스가 계속 진행됩니다.</li>
 *     <li>2xx (Successful): 요청이 성공적으로 수신되었고 이해되며 처리되었습니다.</li>
 *     <li>3xx (Redirection): 요청을 완료하려면 추가 동작이 필요합니다.</li>
 *     <li>4xx (Client Error): 요청에 잘못된 구문이 포함되어 있거나 충족할 수 없어서 이해할 수 없습니다.</li>
 *     <li>5xx (Server Error): 서버가 유효한 요청을 수행하지 못했습니다.</li>
 * </ol>
 *
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6">RFC7231 - Response Status Codes</a>
 * @see <a href="https://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml">IANA - HTTP Status Code Registry</a>
 */
public enum HttpStatus {
    /**
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.3.1">RFC7231 - 200 OK</a>
     */
    OK(200, "OK"),

    /**
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.3.2">RFC7231 - 201 Created</a>
     */
    CREATED(201, "Created"),

    /**
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.3.5">RFC7231 - 204 No Content</a>
     */
    NO_CONTENT(204, "No Content"),

    /**
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.1">RFC7231 - 400 Bad Request</a>
     */
    BAD_REQUEST(400, "Bad Request"),

    /**
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.4">RFC7231 - 404 Not Found</a>
     */
    NOT_FOUND(404, "Not Found"),

    /**
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.5">RFC7231 - 405 Method Not Allowed</a>
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    /**
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.1">RFC7231 - 500 Internal Server Error</a>
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return this.message;
    }
}
