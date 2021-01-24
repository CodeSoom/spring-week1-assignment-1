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

public class DemoHttpHandler implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Task> tasks = new ArrayList<>();
    private Long newId = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println("method : " + method + " path : " + path);

        switch (method) {
            case "GET"      : handleGet(path, exchange);
                              break;
            case "POST"     : handlePost(exchange);
                              break;
            case "PUT"      : handlePut(path, exchange);
                              break;
            case "DELETE"   : handleDelete(path, exchange);
                              break;
            default         : responseSend(HttpStatusCode.NOT_FOUND.getCode(), "method error", exchange);
        }
    }

    private void handleGet(String path, HttpExchange exchange)
            throws IOException {
        if (path.equals("/tasks")) {
            responseSend(HttpStatusCode.OK.getCode(), tasksToJSON(), exchange);
        }

        if (path.startsWith("/tasks/")) {
            Long id = Long.parseLong(path.substring("/tasks/".length()));
            Task task = findTask(id);
            if (task == null) {
                responseSend(HttpStatusCode.NOT_FOUND.getCode(), "id error", exchange);
                return;
            }
            responseSend(HttpStatusCode.OK.getCode(), toJSON(task), exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        Task task = toTask(getBody(exchange));
        task.setId(generateId());
        tasks.add(task);
        responseSend(HttpStatusCode.CREATED.getCode(), toJSON(task), exchange);
    }

    private void handlePut(String path, HttpExchange exchange) throws IOException {
        if (path.startsWith("/tasks/")) {
            Long id = Long.parseLong(path.substring("/tasks/".length()));
            Task task = findTask(id);
            System.out.println("zz " + task);
            if (task == null) {
                responseSend(HttpStatusCode.NOT_FOUND.getCode(), "id error", exchange);
                return;
            }
            Task source = toTask(getBody(exchange));
            task.setTitle(source.getTitle());
            responseSend(HttpStatusCode.OK.getCode(), toJSON(task), exchange);
        }
    }

    private void handleDelete(String path, HttpExchange exchange) throws IOException {
        if (path.startsWith("/tasks/")) {
            Long id = Long.parseLong(path.substring("/tasks/".length()));
            Task task = findTask(id);
            if (task == null) {
                responseSend(HttpStatusCode.NOT_FOUND.getCode(), "id error", exchange);
                return;
            }
            tasks.remove(task);
            responseSend(HttpStatusCode.OK.getCode(), toJSON(task), exchange);
        }
    }

    private Task findTask(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void responseSend(int statusCode, String content, HttpExchange exchange)
            throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String toJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);

        return outputStream.toString();
    }

    private Long generateId() {
        return newId += 1;
    }

    private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
