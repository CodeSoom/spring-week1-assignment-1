package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.codesoom.assignment.HttpStatus.BAD_REQUEST;
import static com.codesoom.assignment.HttpStatus.CREATED;
import static com.codesoom.assignment.HttpStatus.NOT_FOUND;
import static com.codesoom.assignment.HttpStatus.NO_CONTENT;
import static com.codesoom.assignment.HttpStatus.OK;

public class TaskHandler implements HttpHandler {
    private final TaskRepository taskRepository = new TaskRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            handleRequest(exchange);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method == null) {
            throw new IllegalArgumentException("method는 null이 올 수 없습니다.");
        }
        String path = exchange.getRequestURI().getPath();
        InputStream in = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(in))
                .lines()
                .collect(Collectors.joining("\n"));

        HttpStatus status = OK;
        String content = "";

        if ("GET".equals(method) && "/tasks".equals(path)) {
            List<Task> tasks = taskRepository.findAll();

            content = objectToJson(tasks);
            response(OK, exchange, content);
            return;

        } else if ("GET".equals(method) && path.startsWith("/tasks/")) {
            Long id = getLongFromPathParameter(path, 2);
            if (id == null) {
                response(BAD_REQUEST, exchange, content);
            }

            Task task = taskRepository.findById(id);
            if (task == null) {
                status = NOT_FOUND;
            } else {
                content = objectToJson(task);
            }
            response(status, exchange, content);
            return;

        } else if ("POST".equals(method)) {
            Task task = requestBodyToObject(body, Task.class);
            Task savedTask = taskRepository.save(task);

            content = objectToJson(savedTask);
            response(CREATED, exchange, content);
            return;

        } else if ("PUT".equals(method)) {
            Long id = getLongFromPathParameter(path, 2);
            if (id == null) {
                response(BAD_REQUEST, exchange, content);
            }

            if (taskRepository.findById(id) == null) {
                status = NOT_FOUND;
            } else {
                Task task = requestBodyToObject(body, Task.class);
                task.setId(id);

                Task updateTask = taskRepository.update(task);

                content = objectToJson(updateTask);

            }
            response(status, exchange, content);
            return;


        } else if ("DELETE".equals(method)) {
            Long id = getLongFromPathParameter(path, 2);
            if (id == null) {
                response(BAD_REQUEST, exchange, content);
            }

            if (taskRepository.findById(id) == null) {
                status = NOT_FOUND;
            } else {
                taskRepository.delete(id);
                status = NO_CONTENT;
            }
            response(status, exchange, content);
            return;
        }

        // 404
        response(NOT_FOUND, exchange, content);

    }

    private Long getLongFromPathParameter(String path, int idx) {
        String[] splitPath = path.split("/");
        if (idx < splitPath.length) {
            return Long.parseLong(splitPath[idx]);
        }
        return null;
    }

    private <T> T requestBodyToObject(String body, Class<T> type) throws JsonProcessingException {
        return objectMapper.readValue(body, type);
    }

    private void response(HttpStatus status, HttpExchange exchange, String content) throws IOException {
        exchange.sendResponseHeaders(status.getCode(), content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes(StandardCharsets.UTF_8));
        responseBody.flush();
        responseBody.close();
    }

    private <T> String objectToJson(T object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        return outputStream.toString();
    }
}
