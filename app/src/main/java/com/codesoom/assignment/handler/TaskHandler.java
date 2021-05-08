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
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TaskHandler implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String serverTimeZone = "Asia/Seoul";
    private static final Logger logger = Logger.getGlobal();

    private final Map<Long, Task> tasks = new HashMap<>();
    private Long newTaskID = 1L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String requestMethod = exchange.getRequestMethod();
        var method = HttpMethod.valueOf(requestMethod);
        final String path = exchange.getRequestURI().getPath();
        logger.log(Level.FINE, () -> method + " " + path);

        String[] pathVariables = path.split("/");
        final var apiTasksLength = 2; // /tasks => "", "tasks"
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

    private void createTask(HttpExchange exchange) throws IOException {
        final String body = readRequestBody(exchange);
        if (body.isBlank()) {
            HttpResponse.text(exchange, HttpStatus.BAD_REQUEST);
            return;
        }

        var task = jsonToTask(body);
        task.setId(newTaskID++);

        var currentLocalTimeInSeoul = LocalDateTime.now(ZoneId.of(serverTimeZone));
        task.setCreatedAt(currentLocalTimeInSeoul);
        task.setLastUpdatedAt(currentLocalTimeInSeoul);

        tasks.put(task.getId(), task);

        HttpResponse.json(exchange, HttpStatus.CREATE.code(), taskToJSON(task));
    }

    private void listTasks(HttpExchange exchange) throws IOException {
        logger.log(Level.FINE, tasksListToJSON(tasks));
        HttpResponse.json(exchange, HttpStatus.OK.code(), tasksListToJSON(tasks));
    }

    private void getTask(HttpExchange exchange, Long taskID) throws IOException {
        var task = tasks.get(taskID);

        if (task == null) {
            HttpResponse.text(exchange, HttpStatus.NOT_FOUND);
            return;
        }

        HttpResponse.json(exchange, HttpStatus.OK.code(), taskToJSON(task));
    }

    private void updateTask(HttpExchange exchange, Long taskID) throws IOException {
        final String body = readRequestBody(exchange);
        var task = tasks.get(taskID);

        if (task == null) {
            HttpResponse.text(exchange, HttpStatus.NOT_FOUND);
            return;
        }

        var updatedTask = jsonToTask(body);
        task.setTitle(updatedTask.getTitle());

        var currentLocalTimeInSeoul = LocalDateTime.now(ZoneId.of(serverTimeZone));
        task.setLastUpdatedAt(currentLocalTimeInSeoul);

        HttpResponse.json(exchange, HttpStatus.OK.code(), taskToJSON(task));
    }

    private void deleteTask(HttpExchange exchange, Long taskID) throws IOException {
        var task = tasks.get(taskID);

        if (task == null) {
            HttpResponse.text(exchange, HttpStatus.NOT_FOUND);
            return;
        }

        var deletedTask = tasks.remove(task.getId());
        logger.log(Level.FINE, "Deleted task: {0}", deletedTask.getTitle());
        HttpResponse.code(exchange, HttpStatus.NO_CONTENT);
    }

    private String readRequestBody(HttpExchange exchange) {
        var inputStream = exchange.getRequestBody();
        var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return new BufferedReader(inputStreamReader)
            .lines()
            .collect(Collectors.joining(System.lineSeparator()));
    }

    private Task jsonToTask(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Task.class);
    }

    private String taskToJSON(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }

    private String tasksListToJSON(Map<Long, Task> tasks) throws JsonProcessingException {
        return objectMapper.writeValueAsString(tasks);
    }
}
