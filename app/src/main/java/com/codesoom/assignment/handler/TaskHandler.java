package com.codesoom.assignment.handler;

import com.codesoom.assignment.http.HttpMethod;
import com.codesoom.assignment.http.HttpResponse;
import com.codesoom.assignment.http.HttpStatus;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHandler implements HttpHandler {
    final private ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks = new ArrayList<>();
    private Long newTaskID = 1L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String requestMethod = exchange.getRequestMethod();
        HttpMethod method = HttpMethod.valueOf(requestMethod);
        final String path = exchange.getRequestURI().getPath();
        System.out.println(method + " " + path);

        String[] pathVariables = path.split("/");
        final int apiTasksLength = 2; // /tasks => "", "tasks"
        if (pathVariables.length > apiTasksLength) {
            handleRequest(exchange, method, path);
            return;
        }

        handleRequest(exchange, method);
    }

    private void handleRequest(HttpExchange exchange, HttpMethod method) throws IOException {
        switch (method) {
            case GET:
                listTasks(exchange);
                break;
            case POST:
                createTask(exchange);
                break;
            default:
                HttpResponse.text(exchange, HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private void handleRequest(HttpExchange exchange, HttpMethod method, String path) throws IOException {
        final Long taskID = Long.parseLong(path.split("/")[2]);

        switch (method) {
            case GET:
                getTask(exchange, taskID);
                break;
            case PUT:
                updateTask(exchange, taskID);
                break;
            case DELETE:
                deleteTask(exchange, taskID);
                break;
            default:
                HttpResponse.text(exchange, HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private String readRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream, Charset.forName("EUC-KR")))
            .lines()
            .collect(Collectors.joining("\n"));
    }

    private void createTask(HttpExchange exchange) throws IOException {
        final String body = readRequestBody(exchange);
        if (body.isBlank()) {
            HttpResponse.text(exchange, HttpStatus.BAD_REQUEST);
            return;
        }

        Task task = toTask(body);
        task.setId(newTaskID++);
        tasks.add(task);

        HttpResponse.json(exchange, HttpStatus.CREATE.code(), taskToJSON(task));
    }

    private void listTasks(HttpExchange exchange) throws IOException {
        HttpResponse.json(exchange, HttpStatus.OK.code(), tasksListToJSON(tasks));
    }

    private void getTask(HttpExchange exchange, Long taskID) throws IOException {
        Task task = tasks.stream()
            .filter(t -> t.getId() == taskID)
            .findFirst()
            .orElse(null);

        if (task == null) {
            HttpResponse.text(exchange, HttpStatus.NOT_FOUND);
            return;
        }

        HttpResponse.json(exchange, HttpStatus.OK.code(), taskToJSON(task));
    }

    private void updateTask(HttpExchange exchange, Long taskID) throws IOException {
        final String body = readRequestBody(exchange);
        Task task = tasks.stream()
            .filter(t -> t.getId() == taskID)
            .findFirst()
            .orElse(null);

        if (task == null) {
            HttpResponse.text(exchange, HttpStatus.NOT_FOUND);
            return;
        }

        String newTitle = toTask(body).getTitle();
        task.setTitle(newTitle);
        HttpResponse.json(exchange, HttpStatus.OK.code(), taskToJSON(task));
    }

    private void deleteTask(HttpExchange exchange, Long taskID) throws IOException {
        Task task = tasks.stream()
            .filter(t -> t.getId() == taskID)
            .findFirst()
            .orElse(null);

        if (task == null) {
            HttpResponse.text(exchange, HttpStatus.NOT_FOUND);
            return;
        }

        if (tasks.remove(task)) {
            HttpResponse.code(exchange, HttpStatus.NO_CONTENT);
            return;
        }

        HttpResponse.text(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Task toTask(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Task.class);
    }

    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    private String tasksListToJSON(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }
}
