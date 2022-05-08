package com.codesoom.assignment;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.codesoom.assignment.HttpMethod.*;

public class DemoHttpHandler implements HttpHandler {
    private final TaskRepository repository;
    private final TaskMapper mapper;

    public DemoHttpHandler(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException, IllegalArgumentException {
        HttpMethod method = httpMethod(exchange);
        String path = httpRequestPath(exchange);
        String body = httpRequestBody(exchange);

        if (path.equals("/tasks")) {
            handleCollection(exchange, method, body);
        }

        if (path.startsWith("/tasks/")) {
            handleItem(exchange, method, path, body);
        }


    }

    private void handleItem(HttpExchange exchange, HttpMethod method, String path, String body) throws IOException {
        try {
            long taskId = extractTaskIdFrom(path);
            final Task task = repository.taskBy(taskId);

            if (method == GET) {
                handleDetail(exchange, task);
            }

            if (method == PUT) {
                handleUpdate(exchange, body, task);
            }

            if (method == DELETE) {
                handleDelete(exchange, task);
            }
        } catch (NoSuchElementException e) {
            sendResponse(exchange, HttpStatus.NOT_FOUND.code(), "taskId에 해당하는 task를 찾을 수 없습니다");
        }
    }

    private void handleCollection(HttpExchange exchange, HttpMethod method, String body) throws IOException {
        if (method == GET) {
            handleList(exchange);
        }

        if (method == POST) {
            handleCreate(exchange, body);
        }
    }

    private void handleDelete(HttpExchange exchange, Task task) throws IOException {
        repository.delete(task.getId());
        sendResponse(exchange, HttpStatus.NO_CONTENT.code(), "정상적으로 삭제되었습니다");
    }

    private void handleUpdate(HttpExchange exchange, String body, Task task) throws IOException {
        final Task newTask = repository.update(task.getId(), mapper.toTask(body));
        sendResponse(exchange, HttpStatus.OK.code(), mapper.toJson(newTask));
    }

    private void handleCreate(HttpExchange exchange, String body) throws IOException {
        final Task newTask = repository.save(mapper.toTask(body));
        sendResponse(exchange, HttpStatus.CREATED.code(), mapper.toJson(newTask));
    }

    private void handleDetail(HttpExchange exchange, Task task) throws IOException {
        final Task foundTask = repository.taskBy(task.getId());
        sendResponse(exchange, HttpStatus.OK.code(), mapper.toJson(foundTask));
    }

    private void handleList(HttpExchange exchange) throws IOException {
        sendResponse(exchange, HttpStatus.OK.code(), mapper.tasksToJson(repository.tasksAll()));
    }


    private void sendResponse(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(content.getBytes());
            outputStream.flush();
        }
    }

    private long extractTaskIdFrom(String path) {
        String taskId = path.split("/")[2];
        return Long.parseLong(taskId);
    }

    private String httpRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("/n"));
    }

    private String httpRequestPath(HttpExchange exchange) {
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

    private HttpMethod httpMethod(HttpExchange exchange) {
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
