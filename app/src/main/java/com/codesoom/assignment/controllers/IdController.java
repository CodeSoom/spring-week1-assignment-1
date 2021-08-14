package com.codesoom.assignment.controllers;

import com.codesoom.assignment.modles.Task;
import com.codesoom.assignment.utils.HttpMethod;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Optional;

public class IdController extends Controller {
    private static final String CAN_NOT_FIND_TASK_EXCEPTION = "Can not find task.";

    private static final String KEY_VALUE_DELIMITER = "=";
    private static final String TITLE_KEY = "title";

    private static final int TITLE_ARRAY_LENGTH = 2;
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;


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

    public void handlePatch(final HttpExchange exchange, final Long taskId, final String requestBody) throws IOException {
        String[] titleArray = requestBody.split(KEY_VALUE_DELIMITER);
        if (!TITLE_KEY.equals(titleArray[KEY_INDEX])) {
            sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, INVALID_REQUEST);
            return;
        }

        try {
            final Task task = getTask(taskId);
            task.setTitle(titleArray[VALUE_INDEX]);
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
