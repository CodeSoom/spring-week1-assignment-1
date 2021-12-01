package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        String content = "Hello, world!";

        if(!body.isBlank()) {
            System.out.println(body);
        }

        if(method.equals("GET") && path.equals("/tasks")) {
            content = "[]";
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.close();

        System.out.println(method + " " + path);
    }
}
