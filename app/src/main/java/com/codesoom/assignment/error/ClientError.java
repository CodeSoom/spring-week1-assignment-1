package com.codesoom.assignment.error;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ClientError {

    public static final void badRequest(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        exchange.getResponseBody().close();
        throw new RuntimeException();
    }

    public static final void notFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        exchange.getResponseBody().close();
        throw new RuntimeException();
    }
}
