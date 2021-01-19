package com.codesoom.assignment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.codesoom.assignment.util.IdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import com.codesoom.assignment.models.Task;

public class DemoHttpHandler implements HttpHandler {

    private List<Task> tasks = new ArrayList<>();
    private IdGenerator idGenerator = new IdGenerator();
    int statusCode = 404;

    public DemoHttpHandler() {
        Task task = new Task();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // REST - CRUD
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        String requestMethod = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
            .lines()
            .collect(Collectors.joining("\n"));

        System.out.println(requestMethod + " " + path);

        String content = "";

        if (requestMethod.equals("GET") && path.equals("/tasks")) {
            content = tasksToJson(tasks);
            this.statusCode = 200;
        }

        if (requestMethod.equals("GET") && path.matches(".*[0-9].*\"")) {
            Task task = tasks.get(0);
            content = taskToJson(task);
            this.statusCode = 200;
        }

        if (requestMethod.equals("POST") && path.equals("/tasks")) {
            if (!requestBody.isBlank()) {
                Task task = toTask(requestBody);
                task.setId(idGenerator.generate());
                tasks.add(task);
            }
            content = tasksToJson(tasks);
            this.statusCode = 201;
        }

        if (requestMethod.equals("PUT") && path.equals("/tasks/1")) {
            Task task = tasks.get(0);
            task.setTitle("과제 제출하기");
            content = taskToJson(task);

            this.statusCode = 200;
        }

        if (requestMethod.equals("DELETE") && path.equals("/tasks/1")) {
            tasks.remove(0);
            this.statusCode = 204;
        }

        exchange.sendResponseHeaders(this.statusCode, content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private String taskToJson(Task task) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String tasksToJson(List<Task> tasks) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private Task toTask(String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(content, Task.class);
    }
}
