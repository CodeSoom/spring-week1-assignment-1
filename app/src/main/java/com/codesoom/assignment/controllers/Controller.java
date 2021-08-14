package com.codesoom.assignment.controllers;

import com.codesoom.assignment.JsonConverter;
import com.codesoom.assignment.services.TaskService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class Controller {
    protected static final String TO_JSON_FAIL = "Json conversion fail.";

    protected final TaskService taskService;

    protected Controller() {
        taskService = new TaskService();
    }

    protected void sendResponse(
            final HttpExchange exchange, final int statusCode, final String content
    ) throws IOException {
        final OutputStream outputStream = exchange.getResponseBody();
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }
    protected void handleGet(final HttpExchange exchange, final Object object) throws IOException {
        final Optional<String> jsonStringOptional = JsonConverter.toJson(object);
        if (jsonStringOptional.isEmpty()) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_JSON_FAIL);
            return;
        }
        sendResponse(exchange, HttpURLConnection.HTTP_OK, jsonStringOptional.get());
    }
}
