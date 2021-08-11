package com.codesoom.assignment;

import com.codesoom.assignment.modles.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class TaskHandler implements HttpHandler {
    public static final String TO_JSON_FAIL = "Json conversion fail.";
    public static final String TO_TASK_FAIL = "Task conversion fail.";

    public static final String HANDLER_PATH = "/tasks";

    private static final String EMPTY_STRING = "";
    private static final String PATH_DELIMITER = "/";

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
        if (HttpMethod.GET.name().equals(method)) {
            sendResponse(exchange, HttpURLConnection.HTTP_OK, HttpMethod.GET.name() + " " + path);
            return;
        }

        if (HttpMethod.PATCH.name().equals(method)) {
            sendResponse(exchange, HttpURLConnection.HTTP_OK, HttpMethod.PATCH.name() + " " + path);
            return;
        }

        if (HttpMethod.DELETE.name().equals(method)) {
            sendResponse(exchange, HttpURLConnection.HTTP_OK, HttpMethod.DELETE.name() + " " + path);
            return;
        }

        sendResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD,
                path + " can only handle "
                + HttpMethod.GET.name() + " "
                + HttpMethod.PATCH.name() + " "
                + HttpMethod.DELETE.name() + " methods.");
    }

    private void handleGet(final HttpExchange exchange) throws IOException {
        final Optional<String> jsonStringOptional = JsonConverter.toJson(tasks);
        if (jsonStringOptional.isEmpty()) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_JSON_FAIL);
            return;
        }
        sendResponse(exchange, HttpURLConnection.HTTP_OK, jsonStringOptional.get());
    }

    private void handlePost(final HttpExchange exchange, final String requestBody) throws IOException {
        final Optional<Task> taskOptional = Task.jsonToTask(requestBody);
        if (taskOptional.isEmpty()) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_TASK_FAIL);
            return;
        }
        tasks.add(taskOptional.get());
        final Optional<String> jsonStringOptional = JsonConverter.toJson(taskOptional.get());
        if (jsonStringOptional.isEmpty()) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_JSON_FAIL);
            return ;
        }
        sendResponse(exchange, HttpURLConnection.HTTP_CREATED, jsonStringOptional.get());
    }

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath().replace(HANDLER_PATH, EMPTY_STRING);

        final String requestBody = new BufferedReader((new InputStreamReader(exchange.getRequestBody())))
                .lines().collect(Collectors.joining(System.lineSeparator()));

        if (!EMPTY_STRING.equals(path)) {
            handleId(exchange, method, path);
            return;
        }

        if (HttpMethod.GET.name().equals(method)) {
            handleGet(exchange);
            return;
        }

        if (HttpMethod.POST.name().equals(method)) {
            handlePost(exchange, requestBody);
            return;
        }

        sendResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD,
                path + " can only handle "
                        + HttpMethod.GET.name() + " "
                        + HttpMethod.POST.name() + " methods");
    }
}
