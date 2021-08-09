package com.codesoom.assignment;

import com.codesoom.assignment.modles.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class TaskHandler implements HttpHandler {
    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getRequestURI().getPath());
        System.out.println(exchange.getRequestMethod());

        final String content = "Hello world";

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.close();

    }
}
