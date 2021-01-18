package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class DemoHttpHandler implements HttpHandler {
    private boolean isValidPath(String path) {
        return path.startsWith("/tasks");
    }

    private Long extractID(String path) {
        String[] split = path.split("/");
        return split.length < 3
                ? null
                : Long.parseLong(split[2]);
    }

    private String get(String path) {
        Long id = extractID(path);
        return id != null ? id.toString() : "ALL";
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println(method + " " + uri.getPath());

        OutputStream outputStream = exchange.getResponseBody();
        String content = "";

        if (isValidPath(path)) {
            switch (method) {
                case "GET" -> content = get(path);
                case "POST" -> content = "POST";
                case "PUT" -> content = "PUT";
                case "PATCH" -> content = "PATCH";
                case "DELETE" -> content = "DELETE";
            }

            exchange.sendResponseHeaders(200, content.length());
        } else {
            content = "not found";
            exchange.sendResponseHeaders(404, content.length());
        }

        outputStream.write(content.getBytes());

        outputStream.flush();
        outputStream.close();
        exchange.close();
    }
}
