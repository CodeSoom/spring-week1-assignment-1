package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MyHandler implements HttpHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Long, Task> taskMap = new LinkedHashMap<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        String[] pathArr = path.split("/");

        String content = null;
        OutputStream outputStream = exchange.getResponseBody();

        if (pathArr.length == 3 && method.equals("GET")) {
            Long id = Long.valueOf(pathArr[2]);
            content = findTaskById(id, exchange);
        }
        if (pathArr.length == 3 && (method.equals("PUT") || method.equals("PATCH"))) {
            Long id = Long.valueOf(pathArr[2]);
            content = editTaskById(id, exchange);
        }

        if (pathArr.length == 3 && method.equals("DELETE")) {
            Long id = Long.valueOf(pathArr[2]);
            content = deleteTaskById(id, exchange);
        }

        if (pathArr.length != 3 && method.equals("GET")) {
            content = findAllTasks(exchange);
        }

        if (pathArr.length != 3 && method.equals("POST")) {
            content = addTask(exchange);
        }

        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String editTaskById(Long id, HttpExchange exchange) throws IOException {
        String content;

        Task originalTask = taskMap.get(id);
        if (originalTask == null) {
            content = "Not Found!";
            exchange.sendResponseHeaders(404, content.getBytes().length);
        } else {
            InputStream inputStream = exchange.getRequestBody();
            String body = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));
            Task newTask = toTask(body);
            originalTask.setTitle(newTask.getTitle());

            content = taskToJson(originalTask);
            exchange.sendResponseHeaders(200, content.getBytes().length);
        }

        return content;
    }

    private String addTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        Task task = toTask(body);
        task.setId();
        taskMap.put(task.getId(), task);

        String content = taskToJson(task);
        exchange.sendResponseHeaders(201, content.getBytes().length);
        return content;
    }

    private String findAllTasks(HttpExchange exchange) throws IOException {
        String content = tasksToJson();
        exchange.sendResponseHeaders(200, content.getBytes().length);
        return content;
    }

    private String deleteTaskById(Long id, HttpExchange exchange) throws IOException {
        String content;
        Task removedTask = taskMap.remove(id);

        if (removedTask == null) {
            content = "Not Found!";
            exchange.sendResponseHeaders(404, content.getBytes().length);
        } else {
            content = "Successfully Deleted!";
            exchange.sendResponseHeaders(200, content.getBytes().length);
        }

        return content;
    }

    private String findTaskById(Long id, HttpExchange exchange) throws IOException {
        String content;
        Task task = taskMap.get(id);

        if (task == null) {
            content = "Not Found!";
            exchange.sendResponseHeaders(404, content.getBytes().length);
        } else {
            content = taskToJson(task);
            exchange.sendResponseHeaders(200, content.getBytes().length);
        }

        return content;
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskMap.values());

        return outputStream.toString();
    }

    private String taskToJson(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
