package com.codesoom.assignment.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponse {
    final static public String charset = "charset=" + StandardCharsets.UTF_8.name();

    public static void code(HttpExchange exchange, HttpStatus status) throws IOException {
        send(exchange, status.code(), null);
    }

    public static void text(HttpExchange exchange, HttpStatus status) throws IOException {
        text(exchange, status.code(), status.message());
    }

    public static void text(HttpExchange exchange, int code, String content) throws IOException {
        final String contentType = "text/html";
        exchange.getResponseHeaders().set("Content-type", String.join("; ", contentType, charset));
        send(exchange, code, content);
    }

    public static void json(HttpExchange exchange, int code, String content) throws IOException {
        final String contentType = "application/json";
        exchange.getResponseHeaders().set("Content-type", String.join("; ", contentType, charset));
        send(exchange, code, content);
    }

    private static void send(HttpExchange exchange, int code, String content) throws IOException {
        if (content == null) {
            exchange.sendResponseHeaders(code, -1);
            return;
        }
        exchange.sendResponseHeaders(code, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
