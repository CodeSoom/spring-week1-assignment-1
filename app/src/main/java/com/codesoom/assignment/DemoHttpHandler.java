package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();


    private List<Task> tasks = new ArrayList<>();

//    public DemoHttpHandler() {
//        Task task = new Task();
//        task.setId(1L);
//        task.setTitle("DO nothing...");
//
//        tasks.add(task);
//
//    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // REST - CURD
        // 1. Method - Get, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        final String method = exchange.getRequestMethod();
        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);
        if (!body.isBlank()) {
            Task task = toTask(body);
            tasks.add(task);
        }

        String content = "Hello, world!";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJason();
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = toPost();
        }

//        if (method.equals("GET") && path.equals("/tasks/")) {
//            content = toSearch();
//        }

        if (method.equals("PUT") && path.equals("/tasks/")) {
            content = "Update title";
        }

        if (method.equals("DELETE") && path.equals("/tasks/")) {
            content = "DELETE task";
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);

    }

//    private Task toSearch(String content) throws JsonProcessingException {
//        return objectMapper.readValue(content, tasks.get(id));
//
//    }

    private String tasksToJason() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String toPost() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String toDelete() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
//    private String searchTask(){
//        objectMapper.writeValue(outputStream, );
//    }

}