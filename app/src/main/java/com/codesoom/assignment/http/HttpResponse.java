package com.codesoom.assignment.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 응답 형식을 결정합니다. 만약 <a href="https://tools.ietf.org/html/rfc7231#section-6.3.5">204
 * No Content</a>처럼 바디를 응답하지 않는다면 아래와 같이 작성할 수 있습니다.
 *
 * <pre>
 *     HttpResponse.code(exchange, HttpStatus.OK);
 * </pre>
 * <p>
 * 플레인 텍스트로 응답한다면 아래와 같이 작성할 수 있습니다.
 *
 * <pre>
 *     HttpResponse.text(exchange, HttpStatus.OK);
 *     HttpResponse.text(exchange, HttpStatus.OK.code(), HttpStatus.OK.message());
 * </pre>
 * <p>
 * JSON과 함께 응답한다면 아래와 같이 작성할 수 있습니다.
 *
 * <pre>
 *     HttpResponse.json(exchange, HttpStatus.OK.code(), jsonObject);
 * </pre>
 */
public class HttpResponse {
    final static public String charset = "charset=" + StandardCharsets.UTF_8.name();

    public static void code(HttpExchange exchange, final HttpStatus status) throws IOException {
        send(exchange, status.code(), null);
    }

    public static void text(HttpExchange exchange, final HttpStatus status) throws IOException {
        text(exchange, status.code(), status.message());
    }

    public static void text(HttpExchange exchange, final int code, final String content) throws IOException {
        final String contentType = "text/html";
        exchange.getResponseHeaders().set("Content-type", String.join("; ", contentType, charset));
        send(exchange, code, content);
    }

    public static void json(HttpExchange exchange, final int code, final String content) throws IOException {
        final String contentType = "application/json";
        exchange.getResponseHeaders().set("Content-type", String.join("; ", contentType, charset));
        send(exchange, code, content);
    }

    private static void send(HttpExchange exchange, final int code, final String content) throws IOException {
        if (content == null) {
            exchange.sendResponseHeaders(code, -1);
            return;
        }
        exchange.sendResponseHeaders(code, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
        // exchange.close();
    }
}
