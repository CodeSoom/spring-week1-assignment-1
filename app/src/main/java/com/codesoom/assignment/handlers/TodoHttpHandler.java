package com.codesoom.assignment.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class TodoHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logRequest(httpExchange);

        String content = "Hello, world!";
        httpExchange.sendResponseHeaders(200, content.getBytes(StandardCharsets.UTF_8).length);

        OutputStream outputStream = httpExchange.getResponseBody();

        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        logResponse(content);
    }

    private void logRequest(HttpExchange httpExchange) {
        String requestMethod = httpExchange.getRequestMethod();
        URI requestURI = httpExchange.getRequestURI();
        String requestPath = requestURI.getPath();

        System.out.println("requestMethod = " + requestMethod);
        System.out.println("requestURI = " + requestURI);
        System.out.println("requestPath = " + requestPath);
        System.out.println();
    }

    private void logResponse(String responseContent) {
        System.out.println("responseContent = " + responseContent);
    }

}
