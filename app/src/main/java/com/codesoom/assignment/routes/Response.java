package com.codesoom.assignment.routes;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Response {
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_NO_CONTENT = 204;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_NOT_FOUND = 404;

    private final int statusCode;
    private final String content;

    public Response(int statusCode, String content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public int statusCode() {
        return statusCode;
    }

    public String content() {
        return content;
    }

    public void send(HttpExchange exchange) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();

        exchange.sendResponseHeaders(statusCode, content.length());

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

        exchange.close();
    }
}
