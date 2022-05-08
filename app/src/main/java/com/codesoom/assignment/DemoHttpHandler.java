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

        try {
            if (method == GET && path.equals("/tasks")) {
                sendResponse(exchange, HttpStatus.OK.code(), mapper.tasksToJson(repository.tasksAll()));
                return;
            }

            if (method == GET && path.startsWith("/tasks/")) {
                long taskId = extractTaskIdFrom(path);
                final Task foundTask = repository.taskBy(taskId);
                sendResponse(exchange, HttpStatus.OK.code(), mapper.toJson(foundTask));
                return;
            }

            if (method == POST && path.equals("/tasks")) {
                final Task newTask = repository.save(mapper.toTask(body));
                sendResponse(exchange, HttpStatus.CREATED.code(), mapper.toJson(newTask));
                return;
            }

            if (method == PUT && path.startsWith("/tasks")) {
                long taskId = extractTaskIdFrom(path);
                final Task newTask = repository.update(taskId, mapper.toTask(body));
                sendResponse(exchange, HttpStatus.OK.code(), mapper.toJson(newTask));
                return;
            }

            if (method == DELETE && path.startsWith("/tasks")) {
                long taskId = extractTaskIdFrom(path);
                repository.delete(taskId);
                sendResponse(exchange, HttpStatus.NO_CONTENT.code(), "정상적으로 삭제되었습니다");
            }
        } catch (NoSuchElementException e) {
            sendResponse(exchange, HttpStatus.NOT_FOUND.code(), "taskId에 해당하는 task를 찾을 수 없습니다");
        }

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
