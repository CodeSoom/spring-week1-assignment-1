package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class DemoHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath();
        System.out.println(method + " " + path);

        String content = "Hello, World!";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = "We have no task.";
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task.";
        }

        exchange.sendResponseHeaders(HttpStatus.OK.code(), content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
        // exchange.close();
    }
}
