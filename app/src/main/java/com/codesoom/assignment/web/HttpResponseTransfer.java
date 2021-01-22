package com.codesoom.assignment.web;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseTransfer {
    public static void sendResponse(HttpResponse response, HttpExchange exchange) throws IOException {
        sendResponse(response.getContent(), response.getCode(), exchange);
    }

    public static void sendResponse(int responseCode, HttpExchange exchange) throws IOException {
        sendResponse("", responseCode, exchange);
    }

    public static void sendResponse(String content, int responseCode, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(responseCode, content.getBytes().length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(content.getBytes());
            outputStream.flush();
        }
    }
}
