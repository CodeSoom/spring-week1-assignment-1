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
 * @author Changsu Im
 * @date 2021-05-05
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-6">RFC7231</a>
 * @since 0.1.0
 */
public enum HttpStatus {
    OK(200, "OK"),

    CREATE(201, "Create"),

    NO_CONTENT(204, "No Content"),

    BAD_REQUEST(400, "Bad Request"),

    NOT_FOUND(404, "Not Found"),

    /**
     * 서버가 알고 있는 요청 메서드이지만 대상 리소스에서 지원되지 않습니다.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.5">RFC7231 - 405 Method Not Allowed</a>
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    final private int code;
    final private String message;

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
