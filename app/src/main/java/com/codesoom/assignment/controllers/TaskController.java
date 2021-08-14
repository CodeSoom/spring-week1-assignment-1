package com.codesoom.assignment.controllers;

import com.codesoom.assignment.JsonConverter;
import com.codesoom.assignment.modles.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;

public class TaskController extends Controller {
    private static final String TO_TASK_FAIL = "Task conversion fail.";

    public void handlePost(final HttpExchange exchange, final String requestBody) throws IOException {
        final Optional<Task> taskOptional = Task.jsonToTask(requestBody);
        if (taskOptional.isEmpty()) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_TASK_FAIL);
            return;
        }
        taskService.setTask(taskOptional.get());
        final Optional<String> jsonStringOptional = JsonConverter.toJson(taskOptional.get());
        if (jsonStringOptional.isEmpty()) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_JSON_FAIL);
            return;
        }
        sendResponse(exchange, HttpURLConnection.HTTP_CREATED, jsonStringOptional.get());
    }
}
