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

public class DemoHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long id = 1L;

    public DemoHttpHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        System.out.println(
                String.format("%s %s Something requests...",
                        method,
                        path
                )
        );

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader((inputStream)))
                .lines()
                .collect(Collectors.joining("\n"));

        int responseCode = 200;
        String content = "";

        if ("GET".equals(method) && "/tasks".equals(path)) {
            content = tasksToJson();
        }

        if ("POST".equals(method) && "/tasks".equals(path)) {

            if (!body.isBlank()) {
                Task task = JsonToTask(body);
                task.setId(id++);
                tasks.add(task);

                responseCode = 201;
                content = taskToJson(task);
            }
        }

        if ("GET".equals(method) && path.startsWith("/tasks/")) {
            Long fetchId = Long.parseLong(path.substring("/tasks/".length()));

            if (fetchId == 0) responseCode = 404;

            if (responseCode == 200 && isExistTask(fetchId)) {
                Task task = fetchOneTask(fetchId);
                content = taskToJson(task);
            }
        }

        byte[] responseBytes = content.getBytes();

        exchange.sendResponseHeaders(responseCode, responseBytes.length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
    }

    private Task JsonToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson() throws IOException {
        if (tasks.size() == 0) return "[]";

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {
        if (task == null) return "{}";

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private boolean isExistTask(Long id) {
        return tasks.stream().anyMatch(t -> t.getId() == id);
    }

    private Task fetchOneTask(Long id) {
        return tasks.stream()
                .filter((t) -> t.getId() == id)
                .findFirst()
                .get();
    }
}
