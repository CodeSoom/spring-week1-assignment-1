package com.codesoom.assignment.models;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Response {
    private int statusCode;
    private String content;

    public Response(int statusCode, String content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public void sendResponse(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(content.getBytes());
            outputStream.flush();
        }
    }
}
