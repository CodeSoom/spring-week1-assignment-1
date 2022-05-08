package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpHandler;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.codesoom.assignment.HttpMethod.*;

public class DemoHttpHandler implements HttpHandler {
    private static final int HTTP_OK_CODE = 200;
    private static final int HTTP_CREATE_CODE = 201;
    private static final int HTTP_NO_CONTENT_CODE = 204;
    private static final int HTTP_NOT_FOUND_CODE = 404;

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
                sendResponse(exchange, HTTP_OK_CODE, mapper.tasksToJson(repository.tasksAll()));
                return;
            }

            if (method == GET && path.startsWith("/tasks/")) {
                long taskId = extractTaskIdFrom(path);
                final Task foundTask = repository.taskBy(taskId);
                sendResponse(exchange, HTTP_OK_CODE, mapper.taskToJson(foundTask));
                return;
            }

            if (method == POST && path.equals("/tasks")) {
                final Task newTask = repository.save(mapper.toTask(body));
                sendResponse(exchange, HTTP_CREATE_CODE, mapper.taskToJson(newTask));
                return;
            }

            if (method == PUT && path.startsWith("/tasks")) {
                long taskId = extractTaskIdFrom(path);
                repository.taskBy(taskId);
                final Task newTask = repository.update(taskId, mapper.toTask(body));
                sendResponse(exchange, HTTP_OK_CODE, mapper.taskToJson(newTask));
                return;
            }

            if (method == DELETE && path.startsWith("/tasks")) {
                long taskId = extractTaskIdFrom(path);
                repository.taskBy(taskId);
                repository.delete(taskId);
                sendResponse(exchange, HTTP_NO_CONTENT_CODE, "정상적으로 삭제되었습니다");
            }
        } catch (NoSuchElementException e) {
            sendResponse(exchange, HTTP_NOT_FOUND_CODE, "taskId에 해당하는 리를 찾을 수 없습니다 from handler layer \n"
                    + e.getMessage() + " from repository layer");
        }

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
