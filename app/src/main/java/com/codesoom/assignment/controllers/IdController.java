package com.codesoom.assignment.controllers;

import com.codesoom.assignment.modles.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class IdController extends Controller {
    private static final String EMPTY_BODY = "";

    public void handleGet(final HttpExchange exchange, final Long taskId) throws  IOException {
        try {
            final Task task = TASK_SERVICE.getTask(taskId);
            sendObject(exchange, HttpURLConnection.HTTP_OK, task);
        } catch (Exception exception) {
            exception.printStackTrace();
            sendResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, exception.getMessage());
        }
    }

    public void handlePatchOrPut(final HttpExchange exchange, final Long taskId, final String requestBody) throws IOException {
        try {
            final Task task = TASK_SERVICE.updateTask(taskId, requestBody);
            sendObject(exchange, HttpURLConnection.HTTP_OK, task);
        } catch (Exception exception) {
            exception.printStackTrace();
            sendResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, exception.getMessage());
        }
    }

    public void handleDelete(final HttpExchange exchange, final Long taskId) throws IOException {
        try {
            TASK_SERVICE.deleteTask(taskId);
            sendResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT, EMPTY_BODY);
        } catch (Exception exception) {
            exception.printStackTrace();
            sendResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, exception.getMessage());
        }
    }
}
