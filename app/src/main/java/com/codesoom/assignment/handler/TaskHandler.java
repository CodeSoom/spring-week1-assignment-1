package com.codesoom.assignment.handler;

import com.codesoom.assignment.http.HttpMethod;
import com.codesoom.assignment.http.HttpResponse;
import com.codesoom.assignment.http.HttpStatus;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
            handleRequestByID(exchange, method, path);
            return;
        }
        handleRequest(exchange, method, path);
    }

    private void handleRequest(HttpExchange exchange, HttpMethod method, String path) throws IOException {
        if (method.equals(HttpMethod.GET)) {
            listTasks(exchange);
            return;
        }

        final String body = readRequestBody(exchange);
        if (body.isBlank()) {
            HttpResponse.text(exchange, HttpStatus.BAD_REQUEST);
            return;
        }

        if (method.equals(HttpMethod.POST)) {
            Task task = toTask(body);
            task.setId(newTaskID++);
            tasks.add(task);

            HttpResponse.json(exchange, HttpStatus.CREATE.code(), taskToJSON(task));
        }
        HttpResponse.text(exchange, HttpStatus.BAD_REQUEST);
    }

    private void listTasks(HttpExchange exchange) throws IOException {
        HttpResponse.json(exchange, HttpStatus.OK.code(), tasksListToJSON(tasks));
    }

    private void handleRequestByID(HttpExchange exchange, HttpMethod method, String path) throws IOException {
        final String taskID = path.split("/")[2];

        if (method.equals(HttpMethod.GET)) {
            Task task = tasks.stream()
                    .filter(t -> t.getId() == Long.parseLong(taskID))
                    .findFirst()
                    .orElse(null);

            if (task == null) {
                HttpResponse.text(exchange, HttpStatus.NOT_FOUND);
            }
            HttpResponse.json(exchange, HttpStatus.OK.code(), taskToJSON(task));
        }

        if (method.equals(HttpMethod.PUT)) {
            final String body = readRequestBody(exchange);
            Task task = tasks.stream()
                    .filter(t -> t.getId() == Long.parseLong(taskID))
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

        if (method.equals(HttpMethod.DELETE)) {
            Task task = tasks.stream()
                    .filter(t -> t.getId() == Long.parseLong(taskID))
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
    }

    private String readRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream, Charset.forName("EUC-KR")))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Task toTask(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Task.class);
    }

    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    private String tasksListToJSON(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }
}
