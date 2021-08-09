package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();

    public DemoHttpHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do nothing...");

        tasks.add(task);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ....
        // 2. Path - "/tasks", "tasks/1", ...
        // 3. Headers, Body(Content)

        String method = httpExchange.getRequestMethod();

        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);
        if (!body.isEmpty()) {
            System.out.println(body);

        }

        String content = "Hello, world!";

        if(method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
        }

        if(method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task.";
        }

        httpExchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private String tasksToJSON() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
