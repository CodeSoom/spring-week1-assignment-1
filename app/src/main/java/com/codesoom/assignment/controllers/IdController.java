package com.codesoom.assignment.controllers;

import com.codesoom.assignment.HttpMethod;
import com.codesoom.assignment.modles.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;

public class IdController extends Controller {
    private static final String CAN_NOT_FIND_TASK_EXCEPTION = "Can not find task.";

    private Task getTask(final Long taskId) throws Exception {
        final Optional<Task> taskOptional = TASK_SERVICE.getTask(taskId);
        if (taskOptional.isEmpty()) {
            throw new Exception(CAN_NOT_FIND_TASK_EXCEPTION);
        }
        return taskOptional.get();
    }

    public void handleGet(final HttpExchange exchange, final Long taskId) throws  IOException {
        try {
            final Task task = getTask(taskId);
            handleGet(exchange, task);
        } catch (Exception exception) {
            exception.printStackTrace();
            sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, exception.getMessage());
        }
    }

    public void handlePatch(final HttpExchange exchange) throws IOException {
        sendResponse(exchange, HttpURLConnection.HTTP_OK, HttpMethod.PATCH.name());
    }

    public void handleDelete(final HttpExchange exchange) throws IOException {
        sendResponse(exchange, HttpURLConnection.HTTP_OK, HttpMethod.DELETE.name());
    }
}
