package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TaskManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomHttpHandler implements HttpHandler {

    private TaskManager taskManager = new TaskManager();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        Task task = setTask(path, requestBody);

        String content = "";
        int resCode = HttpStatus.OK.getValue();
        List<Task> result = new ArrayList<>();
        if (isTask(path)) {
            if ("GET".equals(method)) {
                if (hasTaskId(path)) {
                    result.add(taskManager.findTask(task));
                    content = taskToJson(result);
                    resCode = HttpStatus.OK.getValue();
                } else {
                    content = taskToJson(taskManager.findTaskAll());
                    resCode = HttpStatus.OK.getValue();
                }
            }
            if ("POST".equals(method)) {
                taskManager.insertTask(task);
                content = taskToJson(result);
                resCode = HttpStatus.CREATED.getValue();
            }
            if ("PUT".equals(method) || "PATCH".equals(method)) {
                if (!hasTaskId(path) || taskManager.findTask(task).isEmpty()) {
                    resCode = HttpStatus.NOT_FOUND.getValue();
                } else {
                    taskManager.updateTask(task);
                    resCode = HttpStatus.OK.getValue();
                }
            }
            if ("DELETE".equals(method)) {
                if (!hasTaskId(path) || taskManager.findTask(task).isEmpty()) {
                    resCode = HttpStatus.NOT_FOUND.getValue();
                } else {
                    taskManager.deleteTask(task);
                    resCode = HttpStatus.NO_CONTENT.getValue();
                }
            }
        }

        exchange.sendResponseHeaders(resCode, content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
    }

    private Task setTask(String path, String requestBody) throws JsonProcessingException {
        Task task = new Task();
        if (!requestBody.isBlank()) {
            task = jsonToTask(requestBody);
        }

        if (hasTaskId(path)) {
            task.setId(Long.valueOf(path.split("/")[2]));
        }

        return task;
    }

    private boolean isTask(String path) {
        return path.startsWith("/tasks");
    }

    private boolean hasTaskId(String path) {
        return path.split("/").length > 2;
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
