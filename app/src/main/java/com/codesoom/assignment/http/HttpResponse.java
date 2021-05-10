package com.codesoom.assignment.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 클라이언트에 응답할 메시지를 생성합니다.
 */
public class HttpResponse {
    private static final String CHARSET = "charset=" + StandardCharsets.UTF_8.name();

    private HttpResponse() {
        throw new IllegalStateException();
    }

    /**
     * 바디 없이 응답합니다.
     *
     * @param exchange
     * @param status
     * @throws IOException
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.3.5">204 No Content</a>
     */
    public static void code(HttpExchange exchange, final HttpStatus status) throws IOException {
        send(exchange, status, null);
    }

    /**
     * 플레인 텍스트(text/html) 형식으로 응답합니다.
     *
     * @param exchange
     * @param status
     * @throws IOException
     */
    public static void text(HttpExchange exchange, final HttpStatus status) throws IOException {
        text(exchange, status, status.message());
    }

    /**
     * 플레인 텍스트(text/html) 형식으로 응답합니다.
     *
     * @param exchange
     * @param status
     * @param content
     * @throws IOException
     */
    public static void text(HttpExchange exchange, final HttpStatus status, final String content) throws IOException {
        final var contentType = "text/html";
        exchange.getResponseHeaders().set("Content-type", String.join("; ", contentType, CHARSET));
        send(exchange, status, content);
    }

    /**
     * JSON(application/json) 형식으로 응답합니다.
     *
     * @param exchange
     * @param status
     * @param content
     * @throws IOException
     */
    public static void json(HttpExchange exchange, final HttpStatus status, final String content) throws IOException {
        final var contentType = "application/json";
        exchange.getResponseHeaders().set("Content-type", String.join("; ", contentType, CHARSET));
        send(exchange, status, content);
    }

    private static void send(HttpExchange exchange, final HttpStatus status, final String content) throws IOException {
        if (content == null) {
            exchange.sendResponseHeaders(status.code(), -1);
            return;
        }

        exchange.sendResponseHeaders(status.code(), content.getBytes().length);
        var outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
        // exchange.close();
    }
}
