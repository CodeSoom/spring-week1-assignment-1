package com.codesoom.assignment.handler;

import com.codesoom.assignment.http.HttpResponse;
import com.codesoom.assignment.http.HttpStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * 루트 경로("/")의 핸들러입니다.
 */
public class ContextRootHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath();
        System.out.println(method + " " + path);

        if (!path.equals("/")) {
            HttpResponse.text(exchange, HttpStatus.NOT_FOUND);
            return;
        }

        HttpResponse.text(exchange, HttpStatus.OK.code(), "Hello, World!");
    }
}
