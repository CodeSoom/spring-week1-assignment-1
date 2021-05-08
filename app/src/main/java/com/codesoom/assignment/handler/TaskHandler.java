package com.codesoom.assignment.handler;

import com.codesoom.assignment.http.HttpMethod;
import com.codesoom.assignment.http.HttpRequest;
import com.codesoom.assignment.http.HttpResponse;
import com.codesoom.assignment.http.HttpStatus;
import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * "/tasks" 경로의 HTTP 요청을 처리합니다.
 */
public class TaskHandler implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
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
        final String json = HttpRequest.readBody(exchange);
        if (json.isBlank()) {
            HttpResponse.text(exchange, HttpStatus.BAD_REQUEST);
            return;
        }

        var task = objectMapper.readValue(json, Task.class);
        task.setId(newTaskID++);
        logger.log(Level.FINE, task.toString());
        tasks.put(task.getId(), task);

        HttpResponse.json(exchange, HttpStatus.CREATE, objectMapper.writeValueAsString(task));
    }

    private void listTasks(HttpExchange exchange) throws IOException {
        var tasksListJSON = objectMapper.writeValueAsString(tasks);
        HttpResponse.json(exchange, HttpStatus.OK, tasksListJSON);
    }

    private void getTask(HttpExchange exchange, Long taskID) throws IOException {
        var task = tasks.get(taskID);

        if (task == null) {
            HttpResponse.text(exchange, HttpStatus.NOT_FOUND);
            return;
        }

        HttpResponse.json(exchange, HttpStatus.OK, objectMapper.writeValueAsString(task));
    }

    private void updateTask(HttpExchange exchange, Long taskID) throws IOException {
        final String json = HttpRequest.readBody(exchange);
        if (json.isBlank()) {
            HttpResponse.text(exchange, HttpStatus.BAD_REQUEST);
            return;
        }

        var task = tasks.get(taskID);
        if (task == null) {
            HttpResponse.text(exchange, HttpStatus.NOT_FOUND);
            return;
        }

        var updatedTask = objectMapper.readValue(json, Task.class);
        task = task.update(updatedTask);
        HttpResponse.json(exchange, HttpStatus.OK, objectMapper.writeValueAsString(task));
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
}
