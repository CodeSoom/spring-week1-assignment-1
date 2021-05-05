package com.codesoom.assignment.handler;

import com.codesoom.assignment.http.HttpStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class BaseHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath();
        System.out.println(method + " " + path);

        if (!path.equals("/")) {
            exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.code(), 0);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.close();
        }

        final String content = "Hello, World!";
        exchange.sendResponseHeaders(HttpStatus.OK.code(), content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
        // exchange.close();
    }
}
