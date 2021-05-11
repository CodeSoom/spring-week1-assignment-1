package com.codesoom.assignment;

import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoRestApiHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI url = exchange.getRequestURI();
        String path = url.getPath();

        if(path.equals("/tasks")) {
            handleCollection(exchange, method);
        }

        if(path.startsWith("/tasks/")) {
            Long id = getId(path);
            handleItem(exchange, method, id);
        }
    }

    private void handleItem(HttpExchange exchange, String method, Long id) throws IOException {
        Task task = findTask(id);

        if(task == null) {
            send(exchange, 404, "");
            return;
        }

        if(method.equals("GET")) {
            send(exchange, 200, taskToJSON(task));
        }

        if(method.equals("PUT") || method.equals("PATCH")) {
            String body = getBody(exchange);

            task.setTitle(toTask(body).getTitle());
            send(exchange, 200, taskToJSON(task));
        }

        if(method.equals("DELETE")) {
            tasks.remove(task);
            send(exchange, 204, "");
        }
    }

    private void handleCollection(HttpExchange exchange, String method) throws IOException {
        if(method.equals("GET")) {
            send(exchange, 200, tasksToJSON());
        }

        if(method.equals("POST")) {
            String body = getBody(exchange);

            Task task = toTask(body);
            task.setId(generateId());
            tasks.add(task);

            send(exchange, 201, taskToJSON(task));
        }
    }

    private Task findTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));
    }
    
    private void send(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();

        outputStream.close();
    }

    private String tasksToJSON() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String taskToJSON(Task task) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private Long getId(String path) {
        return Long.parseLong(path.substring("/tasks/".length()));
    }

    private Long generateId() {
        newId += 1;
        return newId;
    }
}
