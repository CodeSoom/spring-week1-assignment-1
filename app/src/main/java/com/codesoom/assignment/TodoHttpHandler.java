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

public class TodoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public TodoHttpHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do nothing...");

        tasks.add(task);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));

        System.out.printf("%s %s %s", method, path, System.lineSeparator());

        if (!body.isBlank()) {
            System.out.println(body);
        }

        String content = "Hello, World!";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
        }

        if (method.equals("GET") && path.split("/")[1].equals("tasks") && path.split("/").length > 2) {
            Long id = Long.valueOf(path.split("/")[2]);

            content = taskToJSON(id);
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task.";

            Task task = toTask(body);
            Long nextId = tasks.get(tasks.size() - 1).getId() + 1;
            task.setId(nextId);

            tasks.add(task);
            System.out.println(task);
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

    private String taskToJSON(Long id) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks
                .stream()
                .filter(task -> task.getId().equals(id))
                .collect(Collectors.toList()));

        return outputStream.toString();
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
