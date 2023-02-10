package com.codesoom.assignment.service;

import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public final List<Task> tasks = new ArrayList<>();
    private Long defaultId = 0L;

    public void RequestSend(int statusCode, HttpExchange exchange, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }


    public String compareConvert(String path, String content, Task task1) throws IOException {
        if (task1.getId().equals(getRequestId(path))) {
            content = tasksToJSON(task1);
        }
        return content;
    }

    public void updateTask(String path, String body) throws JsonProcessingException {
        Task task = toTask(body);
        task.setId(getRequestId(path));
        tasks.set(getRequestId(path).intValue() - 1, task);
    }

    public void createNewTask(String body) throws JsonProcessingException {
        Task task = toTask(body);
        task.setId(autoIncrementId());
        tasks.add(task);
    }

    private Long autoIncrementId() {
        return defaultId += 1L;
    }


    public String getBody(HttpExchange exchange) {
        return new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public Long getRequestId(String path) {
        return Long.parseLong(path.split("/")[2]);
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    public String tasksToJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);
        return outputStream.toString();
    }

    public String detailContent(String path, String content) throws IOException {
        for (Task task1 : tasks) {
            content = compareConvert(path, content, task1);
        }
        return content;
    }
}
