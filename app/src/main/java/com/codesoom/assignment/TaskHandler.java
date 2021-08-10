package com.codesoom.assignment;

import com.codesoom.assignment.modles.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class TaskHandler implements HttpHandler {
    public static final String TO_JSON_FAIL = "Json conversion fail.";
    public static final String TO_TASK_FAIL = "Task conversion fail.";

    public static final String HANDLER_PATH = "/tasks";

    private static final String EMPTY_STRING = "";
    private static final String PATH_DELIMITER = "/";

    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_PUT = "PUT";
    private static final String HTTP_PATCH = "PATCH";
    private static final String HTTP_DELETE = "DELETE";

    private final List<Task> tasks = new ArrayList<>();

    private void sendResponse(
            final HttpExchange exchange, final int statusCode, final String content
    ) throws IOException {
        final OutputStream outputStream = exchange.getResponseBody();
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

    private void handleId(
            final HttpExchange exchange, final String method, final String path
    ) throws IOException {
        if (HTTP_PUT.equals(method)) {
            sendResponse(exchange, HttpURLConnection.HTTP_OK, HTTP_PUT + " " + path);
        }

        if (HTTP_PATCH.equals(method)) {
            sendResponse(exchange, HttpURLConnection.HTTP_OK, HTTP_PATCH + " " + path);
        }

        if (HTTP_DELETE.equals(method)) {
            sendResponse(exchange, HttpURLConnection.HTTP_OK, HTTP_DELETE + " " + path);
        }

        sendResponse(exchange, HttpURLConnection.HTTP_OK, HTTP_GET + " " + path);
    }

    private void handleGet(final HttpExchange exchange) throws IOException {
        final String content = JsonConverter.toJsonOrNull(tasks);
        if (content == null) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_JSON_FAIL);
            return;
        }
        sendResponse(exchange, HttpURLConnection.HTTP_OK, content);
    }

    private void handlePost(final HttpExchange exchange, final String requestBody) throws IOException {
        final Task task = JsonConverter.jsonToTaskOrNull(requestBody);
        if (task == null) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_TASK_FAIL);
            return;
        }
        final String content = JsonConverter.toJsonOrNull(task);
        if (content == null) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_JSON_FAIL);
            return ;
        }
        tasks.add(task);
        sendResponse(exchange, HttpURLConnection.HTTP_CREATED, content);
    }

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath().replace(HANDLER_PATH, EMPTY_STRING);

        final String requestBody = new BufferedReader((new InputStreamReader(exchange.getRequestBody())))
                .lines().collect(Collectors.joining(System.lineSeparator()));

        if (!EMPTY_STRING.equals(path)) {
            handleId(exchange, method, path);
        }

        if (HTTP_GET.equals(method)) {
            handleGet(exchange);
            return;
        }
        handlePost(exchange, requestBody);
    }
}
