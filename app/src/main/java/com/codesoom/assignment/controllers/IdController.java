package com.codesoom.assignment.controllers;

import com.codesoom.assignment.modles.Task;
import com.codesoom.assignment.utils.HttpMethod;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class IdController extends Controller {

    public void handleGet(final HttpExchange exchange, final Long taskId) throws  IOException {
        try {
            final Task task = TASK_SERVICE.getTask(taskId);
            handleGet(exchange, task);
        } catch (Exception exception) {
            exception.printStackTrace();
            sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, exception.getMessage());
        }
    }

    public void handlePatch(final HttpExchange exchange, final Long taskId, final String requestBody) throws IOException {
        try {
            final Task task = TASK_SERVICE.updateTask(taskId, requestBody);
            handleGet(exchange, task);
        } catch (Exception exception) {
            exception.printStackTrace();
            sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, exception.getMessage());
        }
    }

    public void handleDelete(final HttpExchange exchange) throws IOException {
        sendResponse(exchange, HttpURLConnection.HTTP_OK, HttpMethod.DELETE.name());
    }
}
