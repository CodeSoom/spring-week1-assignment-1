package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentHttpHandler implements HttpHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();

        String content = "Hello World!";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = getAllTasks();
            response(exchange, content, 200);
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            InputStream requestBody = exchange.getRequestBody();

            String body = toBody(requestBody);

            if (body.isEmpty()) {
                content = "Empty body";
                exchange.sendResponseHeaders(400, content.getBytes().length);
                response(exchange, content, 400);
            }

            Task task = toTask(body);

            if (task.getTitle() == null) {
                content = "Title must be required.";
                response(exchange, content, 400);
            }

            addTask(task);
            content = getOneTask(task.getId());
            response(exchange, content, 201);
        }
    }

    private String getAllTasks() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private String getOneTask(Long id) throws IOException {
        Task findTask = tasks.stream().filter(task -> task.getId().equals(id)).findFirst()
            .orElse(null);
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, findTask);
        return outputStream.toString();
    }

    private void addTask(Task task) {
        tasks.add(task);
    }

    private String toBody(InputStream requestBody) {
        return new BufferedReader(new InputStreamReader(requestBody))
            .lines()
            .collect(Collectors.joining("\n"));
    }

    private Task toTask(String content) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        task.setId((long) (tasks.size() + 1));
        return task;
    }

    private void response(HttpExchange exchange, String content, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
