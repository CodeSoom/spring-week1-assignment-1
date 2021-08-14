package com.codesoom.assignment.controllers;

import com.codesoom.assignment.JsonConverter;
import com.codesoom.assignment.modles.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;

public class TaskController extends Controller {
    private static final String TO_TASK_FAIL = "Task conversion fail.";

    public void handleGet(final HttpExchange exchange) throws  IOException {
        super.handleGet(exchange, TASK_SERVICE.getTasks());
    }

    public void handlePost(final HttpExchange exchange, final String requestBody) throws IOException {
        try {
            final Task task = TASK_SERVICE.createTask(requestBody);
            final Optional<String> jsonStringOptional = JsonConverter.toJson(task);
            if (jsonStringOptional.isEmpty()) {
                sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_JSON_FAIL);
                return;
            }
            sendResponse(exchange, HttpURLConnection.HTTP_CREATED, jsonStringOptional.get());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_TASK_FAIL);
        }
    }
}
