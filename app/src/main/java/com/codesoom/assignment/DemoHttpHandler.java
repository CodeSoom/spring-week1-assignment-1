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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import com.codesoom.assignment.models.Task;

public class DemoHttpHandler implements HttpHandler {

    private List<Task> tasks = new ArrayList<>();

    public DemoHttpHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do nothing....");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Do something");

        tasks.add(task);
        tasks.add(task2);
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

        String content = "Hello, World";

        if (requestMethod.equals("GET") && path.equals("/tasks")) {
            content = tasksToJson();
        }

        if (requestMethod.equals("POST") && path.equals("/tasks")) {
            if (!requestBody.isBlank()) {
                Task task = toTask(requestBody);
                tasks.add(task);
            }
            content = tasksToJson();
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private String tasksToJson() throws IOException {
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
