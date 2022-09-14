package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHandler implements HttpHandler {
    TaskRepository taskRepository = new TaskRepository();
    ObjectMapper objectMapper = new ObjectMapper();

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

        int code = 200;
        String content = "";

        if ("GET".equals(method) && "/tasks".equals(path)) {
            List<Task> tasks = taskRepository.findAll();

            content = objectToJson(tasks);
            response(code, exchange, content);
            return;

        } else if ("GET".equals(method) && path.startsWith("/tasks/")) {
            Long id = getLongFromPathParameter(path, 2);
            if (id == null) {
                response(400, exchange, content);
            }

            Task task = taskRepository.findById(id);
            if (task == null) {
                code = 404;
            } else {
                content = objectToJson(task);
            }
            response(code, exchange, content);
            return;

        } else if ("POST".equals(method)) {
            Task task = requestBodyToObject(body, Task.class);
            Task savedTask = taskRepository.save(task);

            content = objectToJson(savedTask);
            response(201, exchange, content);
            return;

        } else if ("PUT".equals(method)) {
            Long id = getLongFromPathParameter(path, 2);
            if (id == null) {
                response(400, exchange, content);
            }

            if (taskRepository.findById(id) == null) {
                code = 404;
            } else {
                Task task = requestBodyToObject(body, Task.class);
                task.setId(id);

                Task updateTask = taskRepository.update(task);

                content = objectToJson(updateTask);

            }
            response(code, exchange, content);
            return;


        } else if ("DELETE".equals(method)) {
            Long id = getLongFromPathParameter(path, 2);
            if (id == null) {
                response(400, exchange, content);
            }

            if (taskRepository.findById(id) == null) {
                code = 404;
            } else {
                taskRepository.delete(id);
                code = 204;
            }
            response(code, exchange, content);
            return;
        }

        // 404
        response(404, exchange, content);

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

    private void response(int code, HttpExchange exchange, String content) throws IOException {
        exchange.sendResponseHeaders(code, content.getBytes().length);
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
