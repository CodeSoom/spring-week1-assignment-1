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
        String[] pathInfo = exchange.getRequestURI().getPath().split("/");
        InputStream inputStream = exchange.getRequestBody();

        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        Task task = null;
        if (!requestBody.isBlank()) {
            task = jsonToTask(requestBody);
        }

        String content = "";
        int resCode = 200;
        List<Task> result = new ArrayList<>();

        if ("GET".equals(method)) {
            if (pathInfo.length > 2) {
                Long taskId = Long.valueOf(pathInfo[2]);
                int index = findById(taskId);
                if (index > 0) {
                    result.add(tasks.get(index));
                    content = taskToJson(result);
                } else {
                    resCode = 404;
                }
            } else {
                content = taskToJson(tasks);
            }
        }
        if ("POST".equals(method)) {
            task.setId((long) tasks.size() + 1);
            tasks.add(task);
            result.add(task);
            content = taskToJson(result);
            resCode = 201;
        }
        if ("PUT".equals(method) || "PATCH".equals(method)) {
            if (pathInfo.length > 2) {
                Long taskId = Long.valueOf(pathInfo[2]);
                int index = findById(taskId);
                if (index > 0) {
                    tasks.get(index).setTitle(task.getTitle());
                    content = taskToJson(tasks);
                } else {
                    resCode = 404;
                }
            } else {
                resCode = 404;
            }
        }
        if ("DELETE".equals(method)) {
            if (pathInfo.length > 2) {
                Long taskId = Long.valueOf(pathInfo[2]);
                int index = findById(taskId);
                if (index > 0) {
                    tasks.remove(index);
                    resCode = 204;
                } else {
                    resCode = 404;
                }
            } else {
                resCode = 404;
            }

        }

        exchange.sendResponseHeaders(resCode, content.getBytes().length);
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
}
