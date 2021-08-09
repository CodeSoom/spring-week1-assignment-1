package com.codesoom.assignment;

import com.codesoom.assignment.modles.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class TaskHandler implements HttpHandler {
    private final List<Task> tasks = new ArrayList<>();

    private void sendResponse(final HttpExchange exchange, final int statusCode, final String content) throws IOException {
        final OutputStream outputStream = exchange.getResponseBody();
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        System.out.println(exchange.getRequestURI().getPath());
        System.out.println(exchange.getRequestMethod());

        final String requestBody = new BufferedReader((new InputStreamReader(exchange.getRequestBody())))
                .lines().collect(Collectors.joining(System.lineSeparator()));

        final Task task = Task.toTaskOrNull(requestBody);
        if (task == null) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, Task.TO_TASK_FAIL);
            return;
        }

        System.out.println(task.toString());

        final String content = task.toJsonOrNull();
        if (content == null) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, Task.TO_JSON_FAIL);
            return;
        }

        sendResponse(exchange, HttpURLConnection.HTTP_OK, content);
    }
}
