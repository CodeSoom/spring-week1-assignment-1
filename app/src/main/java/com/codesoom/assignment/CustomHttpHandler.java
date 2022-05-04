package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomHttpHandler implements HttpHandler {

    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        InputStream inputStream = exchange.getRequestBody();

        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        Task task = null;
        if (!requestBody.isBlank()) {
            task = jsonToTask(requestBody);
        }

        String content = "";
        List<Task> result = new ArrayList<>();
        if(hasTaskId(path)) {
            if ("PUT".equals(method) || "PACTH".equals(method)) {
                int index = findById(task.getId());
                tasks.get(index).setTitle(task.getTitle());
                result.add(tasks.get(index));
                content = taskToJson(result);
            } else if ("DELETE".equals(method)) {
                tasks.remove(task);
                content = taskToJson(tasks);
            } else if ("GET".equals(method)) {
                content = taskToJson(result);
            }
        } else {
            if ("GET".equals(method)) {
                content = taskToJson(tasks);
            } else if ("POST".equals(method)) {
                task.setId((long) tasks.size());
                tasks.add(task);
                content = taskToJson(tasks);
            }
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
    }

    private int findById(Long id) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private String taskToJson(List<Task> taskList) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskList);

        return outputStream.toString();
    }

    private Task jsonToTask(String requestBody) throws JsonProcessingException {
        return objectMapper.readValue(requestBody, Task.class);
    }

    private boolean hasTaskId(String path) {
        return path.startsWith("/tasks/") && path.split("/").length > 2;
    }
}
