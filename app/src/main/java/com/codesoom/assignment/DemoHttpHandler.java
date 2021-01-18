package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class DemoHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        System.out.println(method + " " + uri.getPath());

        OutputStream outputStream = exchange.getResponseBody();
        String content = "";

        switch (method) {
            case "GET" -> content = "GET";
            case "POST" -> content = "POST";
            case "PUT" -> content = "PUT";
            case "PATCH" -> content = "PATCH";
            case "DELETE" -> content = "DELETE";
        }

        exchange.sendResponseHeaders(200, content.length());
        outputStream.write(content.getBytes());

        outputStream.flush();
        outputStream.close();
        exchange.close();
    }
}
