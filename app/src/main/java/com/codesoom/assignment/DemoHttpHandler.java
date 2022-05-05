package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpHandler;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.codesoom.assignment.HttpMethod.*;

public class DemoHttpHandler implements HttpHandler {
    private static final int HTTP_OK_CODE = 200;
    private static final int HTTP_CREATE_CODE = 201;
    private static final int HTTP_NO_CONTENT_CODE = 204;
    private static final int HTTP_NOT_FOUND_CODE = 404;
    private final TaskRepository repository;

    public DemoHttpHandler(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException, IllegalArgumentException {
        ObjectMapper mapper = new ObjectMapper();

        HttpMethod method = getHttpMethod(exchange);
        String path = getHttpRequestPath(exchange);
        String body = getHttpRequestBody(exchange);

        if (method == GET && path.equals("/tasks")) {
            sendResponse(exchange, HTTP_OK_CODE, tasksToJson(mapper));
        }

        if (method == GET && path.startsWith("/tasks/")) {
            long taskId = extractTaskIdFrom(path);
            Task foundTask = repository.taskBy(taskId);

            if (foundTask == null) {
                sendResponse(exchange, HTTP_NOT_FOUND_CODE, "TaskId가 유효하지 않습니다");
            } else {
                sendResponse(exchange, HTTP_OK_CODE, taskToJson(mapper, foundTask));
            }
        }

        if (method == POST && path.equals("/tasks")) {
            Task newTask = toTask(mapper, body);
            repository.save(newTask);
            sendResponse(exchange, HTTP_CREATE_CODE, taskToJson(mapper, newTask));
        }

        if (method == PUT && path.startsWith("/tasks")) {
            long taskId = extractTaskIdFrom(path);
            Task foundTask = repository.taskBy(taskId);

            if (foundTask == null) {
                sendResponse(exchange, HTTP_NOT_FOUND_CODE, "TaskId가 유효하지 않습니다");
            } else {
                Task newTask = toTask(mapper, body);
                newTask = repository.update(taskId, newTask);
                sendResponse(exchange, HTTP_OK_CODE, taskToJson(mapper, newTask));
            }

        }

        if (method == DELETE && path.startsWith("/tasks")) {
            long taskId = extractTaskIdFrom(path);
            Task foundTask = repository.taskBy(taskId);

            if (foundTask == null) {
                sendResponse(exchange, HTTP_NOT_FOUND_CODE, "TaskId가 유효하지 않습니다");
            } else {
                repository.delete(taskId);
                sendResponse(exchange, HTTP_NO_CONTENT_CODE, "정상적으로 삭제되었습니다");
            }
        }
    }


    private Task toTask(ObjectMapper mapper, String content) throws JsonProcessingException {
        return mapper.readValue(content, Task.class);
    }

    private String tasksToJson(ObjectMapper mapper) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, repository.tasksAll());

        return outputStream.toString();
    }

    private String taskToJson(ObjectMapper mapper, Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private void sendResponse(HttpExchange exchange, int statudCode, String content) throws IOException {
        exchange.sendResponseHeaders(statudCode, content.getBytes().length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(content.getBytes());
            outputStream.flush();
        }
    }

    private long extractTaskIdFrom(String path) {
        String taskId = path.split("/")[2];
        return Long.parseLong(taskId);
    }

    private String getHttpRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("/n"));
    }

    private String getHttpRequestPath(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        if (uri == null) {
            throw new IllegalArgumentException("failed to return URI");
        }

        String path = uri.getPath();
        if (path == null) {
            throw new IllegalArgumentException("failed to return request path");
        }

        return path;
    }

    private HttpMethod getHttpMethod(HttpExchange exchange) {
        String methodInString = exchange.getRequestMethod();
        return Arrays.stream(HttpMethod.values())
                .filter((method) ->
                        method.toString().equalsIgnoreCase(methodInString)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "failed to return HttpMethod enum value"
                        )
                );
    }
}
