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

public class TodoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private String content = "";
    private Integer statusCode = 500;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        boolean isTasksPath = "/tasks".equals(path);
        boolean isTasksPathWithId = path.length() > "/tasks/".length();
        Long id = Long.parseLong(path.substring(7));

        if ("GET".equals(method) && isTasksPath) {
            handleGetRequest();
        }

        if ("GET".equals(method) && isTasksPathWithId) {
            handleGetRequest(id);
        }

        if ("POST".equals(method) && isTasksPath && !body.isBlank()) {
            handlePostRequest(id, body);
        }

        if ("PUT".equals(method) && isTasksPathWithId && !body.isBlank()) {
            handlePutRequest(id, body);
        }

        if ("DELETE".equals(method) && isTasksPathWithId) {
            handleDeleteRequest(id);
        }

        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void handleGetRequest() throws IOException {
        content = tasksToJson(tasks);
        statusCode = HttpStatus.Ok.code();
    }

    private void handleGetRequest(Long id) throws IOException {
        List<Task> task = tasks.stream()
                .filter(item -> id.equals(item.getId()))
                .collect(Collectors.toList());

        if(task.size() != 0){
            content = taskToJson(task.get(0));
            statusCode = HttpStatus.Ok.code();
        } else {
            statusCode = HttpStatus.NotFound.code();
        }
    }

    private void handlePostRequest(Long id, String body) throws IOException {
        String title = toTask(body).getTitle();

        List<Task> newTasks = tasks.stream()
                .map(item -> id.equals(item.getId())
                        ? new Task(id, title)
                        : item)
                .collect(Collectors.toList());

        if(!tasks.equals(newTasks)) {
            tasks = newTasks;
            statusCode = HttpStatus.Ok.code();
            content = taskToJson(new Task(id, title));
        } else {
            statusCode = HttpStatus.NotFound.code();
        }
    }

    private void handlePutRequest(Long id, String body) throws IOException {
        String title = toTask(body).getTitle();

        List<Task> newTasks = tasks.stream()
                .map(item -> id.equals(item.getId())
                        ? new Task(id, title)
                        : item)
                .collect(Collectors.toList());

        if(!tasks.equals(newTasks)) {
            tasks = newTasks;
            statusCode = HttpStatus.Ok.code();
            content = taskToJson(new Task(id, title));
        } else {
            statusCode = HttpStatus.NotFound.code();
        }
    }

    private void handleDeleteRequest(Long id) throws IOException {
        List<Task> remainingTasks = tasks.stream()
                .filter(item -> !id.equals(item.getId()))
                .collect(Collectors.toList());

        if(remainingTasks.size() != tasks.size()){
            tasks = remainingTasks;
            statusCode = HttpStatus.NoContent.code();
            content = tasksToJson(tasks);
        } else {
            statusCode = HttpStatus.NotFound.code();
        }
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }
}
