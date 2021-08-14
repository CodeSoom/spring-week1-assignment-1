package com.codesoom.assignment;

import com.codesoom.assignment.controllers.IdController;
import com.codesoom.assignment.controllers.TaskController;
import com.codesoom.assignment.modles.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class TaskHandler implements HttpHandler {
    private static final String TO_JSON_FAIL = "Json conversion fail.";
    private static final String TO_TASK_FAIL = "Task conversion fail.";

    private static final String INVALID_REQUEST = "Invalid request.";
    private static final String INVALID_ID = "Invalid id.";

    public static final String HANDLER_PATH = "/tasks";

    private static final int INDEX_START = 0;
    private static final int ID_INDEX = 1;

    private static final String EMPTY_STRING = "";
    private static final String PATH_DELIMITER = "/";

    private final Map<Long, Task> tasks = new HashMap<>();


    private final TaskController taskController;
    private final IdController idController;

    public TaskHandler() {
        taskController = new TaskController();
        idController = new IdController();
    }

    private Optional<Long> parseId(final String idString) {
        Long taskId = null;
        try {
            taskId = Long.parseLong(idString);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.ofNullable(taskId);
    }

    private void sendResponse(
            final HttpExchange exchange, final int statusCode, final String content
    ) throws IOException {
        final OutputStream outputStream = exchange.getResponseBody();
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.close();
    }

    private void handleInvalidMethod(
        final HttpExchange exchange, final String path, final String[] allowedMethods
    ) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HANDLER_PATH).append(path).append(" can only handle ");
        Arrays.stream(allowedMethods).forEach(method ->stringBuilder.append(method).append(" "));
        stringBuilder.append("methods.");
        sendResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD, stringBuilder.toString());
    }

    private void handleGet(final HttpExchange exchange, final Object object) throws IOException {
        final Optional<String> jsonStringOptional = JsonConverter.toJson(object);
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
        tasks.put(taskOptional.get().getId(), taskOptional.get());
        final Optional<String> jsonStringOptional = JsonConverter.toJson(taskOptional.get());
        if (jsonStringOptional.isEmpty()) {
            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, TO_JSON_FAIL);
            return;
        }
        sendResponse(exchange, HttpURLConnection.HTTP_CREATED, jsonStringOptional.get());
    }

    private void handleId(
            final HttpExchange exchange, final String method, final String path
    ) throws IOException {

        if (path.charAt(INDEX_START) != PATH_DELIMITER.charAt(INDEX_START)) {
            sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, INVALID_REQUEST);
            return;
        }

        final String[] paths = path.split(PATH_DELIMITER);
        if (ID_INDEX + 1 != paths.length) {
            sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, INVALID_REQUEST);
            return;
        }

        final Optional<Long> taskIdOptional = parseId(paths[ID_INDEX]);
        if (taskIdOptional.isEmpty()) {
            sendResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, INVALID_ID);
            return;
        }

        if (HttpMethod.GET.name().equals(method)) {
            idController.handleGet(exchange, taskIdOptional.get());
            return;
        }

        if (HttpMethod.PATCH.name().equals(method)) {
            idController.handlePatch(exchange);
            return;
        }

        if (HttpMethod.DELETE.name().equals(method)) {
            idController.handleDelete(exchange);
            return;
        }

        final String[] allowedMethods = new String[] {HttpMethod.GET.name(), HttpMethod.PATCH.name(), HttpMethod.DELETE.name()};
        handleInvalidMethod(exchange, path, allowedMethods);
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
            taskController.handleGet(exchange);
            return;
        }

        if (HttpMethod.POST.name().equals(method)) {
            taskController.handlePost(exchange, requestBody);
            return;
        }

        final String[] allowedMethods = new String[] {HttpMethod.GET.name(), HttpMethod.POST.name()};
        handleInvalidMethod(exchange, path, allowedMethods);
    }
}
