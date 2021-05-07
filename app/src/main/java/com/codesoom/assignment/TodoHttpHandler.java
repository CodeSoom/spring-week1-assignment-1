package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {

    private Long newTaskId = 1L;

    private List<Task> taskList = new ArrayList<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String content = "";

        InputStream inputStream = httpExchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        sendResponse(httpExchange, requestMethod, path, requestBody, content);
    }

    private void sendResponse(HttpExchange httpExchange, String requestMethod, String path, String requestBody, String content) throws IOException {
        // Get TaskList
        if (requestMethod.equals(HttpMethod.GET.getName()) && path.equals("/tasks")) {
            content = toJson(taskList);
            httpExchange.sendResponseHeaders(HttpStatus.OK.getCode(), content.getBytes().length);
        }

        // Get Task
        if (requestMethod.equals(HttpMethod.GET.getName()) && path.startsWith("/tasks/")) {
            Long taskId = Long.parseLong(path.substring(7));
            Task searchTask = taskList.stream()
                                      .filter(task -> task.getId().equals(taskId))
                                      .findAny()
                                      .orElse(null);
            if (searchTask == null) {
                httpExchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            } else {
                content = toJson(searchTask);
                httpExchange.sendResponseHeaders(HttpStatus.OK.getCode(), content.getBytes().length);
            }
        }

        // Create Task
        if (requestMethod.equals(HttpMethod.POST.getName()) && path.equals("/tasks")) {
            Task task = JsonToTask(requestBody);
            task.setId(newTaskId);
            newTaskId++;
            taskList.add(task);
            content = toJson(task);
            httpExchange.sendResponseHeaders(HttpStatus.CREATED.getCode(), content.getBytes().length);
        }

        // Update Task
        if ((requestMethod.equals(HttpMethod.PUT.getName()) || requestMethod.equals(HttpMethod.PATCH.getName())) && path.startsWith("/tasks/")) {
            Task requestTask = JsonToTask(requestBody);
            Long taskId = Long.parseLong(path.substring(7));
            Task searchTask = taskList.stream()
                                      .filter(task -> task.getId().equals(taskId))
                                      .findAny()
                                      .orElse(null);
            if (searchTask == null) {
                httpExchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            } else {
                searchTask.setTitle(requestTask.getTitle());
                content = toJson(searchTask);
                httpExchange.sendResponseHeaders(HttpStatus.OK.getCode(), content.getBytes().length);
            }
        }

        // Delete Task
        if (requestMethod.equals(HttpMethod.DELETE.getName()) && path.startsWith("/tasks/")) {
            Long taskId = Long.parseLong(path.substring(7));
            Task searchTask = taskList.stream()
                                      .filter(task -> task.getId().equals(taskId))
                                      .findAny()
                                      .orElse(null);
            if (searchTask == null) {
                httpExchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            } else {
                taskList.remove(searchTask);
                httpExchange.sendResponseHeaders(HttpStatus.NO_CONTENT.getCode(), content.getBytes().length);
            }
        }

        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private Task JsonToTask(String requestBody) throws JsonProcessingException {
        return objectMapper.readValue(requestBody, Task.class);
    }

    private String toJson(Object taskObject) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskObject);

        return outputStream.toString();
    }
}
