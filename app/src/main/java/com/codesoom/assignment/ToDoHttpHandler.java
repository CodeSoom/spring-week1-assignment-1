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

public class ToDoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long lastId = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);

        if (!body.isBlank()) {
            System.out.println(body);
        }

        String content = "";
        int responseCode = 404;

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
            responseCode = 200;
        } else if (method.equals("POST") && path.equals("/tasks")) {
            Task task = toTask(body);
            tasks.add(task);
            content = toString(task);
            responseCode = 201;
        }

        exchange.sendResponseHeaders(responseCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        task.setId(lastId++);
        return task;
    }

    private String toString(Task task) throws IOException {
        return objectMapper.writeValueAsString(task);
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
