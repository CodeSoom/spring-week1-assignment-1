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
    public static final String HANDLER_PATH = "/tasks";

    private static final String EMPTY_STRING = "";
    private static final String PATH_DELIMITER = "/";

    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_PUT = "PUT";
    private static final String HTTP_PATCH = "PATCH";
    private static final String HTTP_DELETE = "DELETE";

    private final List<Task> tasks = new ArrayList<>();

    private void sendResponse(final HttpExchange exchange, final int statusCode, final String content) throws IOException {
        final OutputStream outputStream = exchange.getResponseBody();
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

    private void handleId(final HttpExchange exchange, final String method, final String path) throws IOException {
        if (method.equals(HTTP_PUT)) {
            sendResponse(exchange, HttpURLConnection.HTTP_OK, HTTP_PUT + " " + path);
            return ;
        }

        if (method.equals(HTTP_PATCH)) {
            sendResponse(exchange, HttpURLConnection.HTTP_OK, HTTP_PATCH + " " + path);
            return ;
        }

        if (method.equals(HTTP_DELETE)) {
            sendResponse(exchange, HttpURLConnection.HTTP_OK, HTTP_DELETE + " " + path);
            return ;
        }

        sendResponse(exchange, HttpURLConnection.HTTP_OK, HTTP_GET + " " + path);
    }

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath().replace(HANDLER_PATH, EMPTY_STRING);

        final String requestBody = new BufferedReader((new InputStreamReader(exchange.getRequestBody())))
                .lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(requestBody);

        if (!path.equals(EMPTY_STRING)) {
            handleId(exchange, method, path);
            return;
        }

        if (method.equals(HTTP_GET)) {
            sendResponse(exchange, HttpURLConnection.HTTP_OK, HTTP_GET + " " + HANDLER_PATH);
            return;
        }

        sendResponse(exchange, HttpURLConnection.HTTP_CREATED, HTTP_POST + " " + HANDLER_PATH);

//
//        final Task task = Task.toTaskOrNull(requestBody);
//        if (task == null) {
//            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, Task.TO_TASK_FAIL);
//            return;
//        }
//
//        System.out.println(task.toString());
//
//        final String content = task.toJsonOrNull();
//        if (content == null) {
//            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, Task.TO_JSON_FAIL);
//            return;
//        }
//
//        sendResponse(exchange, HttpURLConnection.HTTP_OK, content);
    }
}
