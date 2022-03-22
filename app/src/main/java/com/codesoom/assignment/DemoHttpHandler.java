package com.codesoom.assignment;

import com.codesoom.assignment.http.HttpStatusCode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

import static com.codesoom.assignment.http.HttpStatusCode.*;


public class DemoHttpHandler implements HttpHandler {

    private static final int EMPTY_RESPONSE_LENGTH = 0;

    public DemoHttpHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();

        if(!path.startsWith("/tasks")) {
            responseHandle(exchange, NOT_FOUND);
            return;
        }

        String httpMethod = exchange.getRequestMethod();

        if(httpMethod.equals("POST")) {
            responseHandle(exchange, CREATED);
            return;
        }

        responseHandle(exchange, OK);
    }

    private void responseHandle(final HttpExchange exchange, final HttpStatusCode statusCode) throws IOException {
        OutputStream responseOutputStream = exchange.getResponseBody();
        exchange.sendResponseHeaders(statusCode.getCode(), EMPTY_RESPONSE_LENGTH);
        responseOutputStream.close();
    }
}
