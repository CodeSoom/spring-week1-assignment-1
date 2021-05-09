package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/tasks")) {
            handleCollection(exchange, method);
            return;
        }

        if (path.startsWith("/tasks/")) {
            Long id = Long.parseLong(path.substring("/tasks/".length()));
            handleItem(exchange, method, id);
            return;
        }
        send(exchange, 200, "Hello");
    }

    private void handleItem(HttpExchange exchange, String method, Long id) throws IOException {
        Task task = findTask(id);

        if (task == null) {
            send(exchange, 404, "");
            return;
        }

        if (method.equals("GET")) {
            send(exchange, 200, tasksToJSON(task));
        }

        if (method.equals("PUT") || method.equals("PATCH")) {
            String body = getBody(exchange);
            Task source = bodyToTask(body);
            task.setTitle(source.getTitle());
            send(exchange, 200, tasksToJSON(task));
        }


        if (method.equals("DELETE")) {
            tasks.remove(task);
            send(exchange, 200, "");
        }
    }

    private Task findTask(Long id) {
        return tasks.stream().filter(task -> task.getId().equals(id))
                .findFirst().orElse(null);
    }

    private void handleCollection(HttpExchange exchange, String method) throws IOException {
        if (method.equals("GET")) {
            send(exchange, 200, tasksToJSON(tasks));
        }
        if (method.equals("POST")) {
            createTask(exchange);
        }
    }

    private void createTask(HttpExchange exchange) throws IOException {
        String body = getBody(exchange);
        Task task = bodyToTask(body);
        task.setId(generateId());
        tasks.add(task);
        send(exchange, 201, tasksToJSON(task));
    }

    private void send(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Long generateId() {
        newId += 1;
        return newId;
    }

    private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private String tasksToJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        return outputStream.toString();
    }

    private Task bodyToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }
}