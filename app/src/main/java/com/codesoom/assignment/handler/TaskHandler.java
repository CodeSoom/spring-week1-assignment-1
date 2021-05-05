package com.codesoom.assignment.handler;

import com.codesoom.assignment.http.HttpMethod;
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
            responseText(exchange, HttpStatus.BAD_REQUEST.code(), HttpStatus.BAD_REQUEST.toString());
            return;
        }

        if (method.equals(HttpMethod.POST)) {
            Task task = toTask(body);
            task.setId(newTaskID++);
            tasks.add(task);

            responseJSON(exchange, HttpStatus.CREATE.code(), taskToJSON(task));
        }
        responseText(exchange, HttpStatus.BAD_REQUEST.code(), HttpStatus.BAD_REQUEST.toString());
    }

    private void listTasks(HttpExchange exchange) throws IOException {
        responseJSON(exchange, HttpStatus.OK.code(), tasksToJSON(tasks));
    }

    private void handleRequestByID(HttpExchange exchange, HttpMethod method, String path) throws IOException {
        final String taskID = path.split("/")[2];

        if (method.equals(HttpMethod.GET)) {
            Task task = tasks.stream()
                    .filter(t -> t.getId() == Long.parseLong(taskID))
                    .findFirst()
                    .orElse(null);

            if (task == null) {
                responseText(exchange, HttpStatus.NOT_FOUND.code(), HttpStatus.NOT_FOUND.toString());
            }
            responseJSON(exchange, HttpStatus.OK.code(), taskToJSON(task));
        }

        if (method.equals(HttpMethod.PUT)) {
            final String body = readRequestBody(exchange);
            Task task = tasks.stream()
                    .filter(t -> t.getId() == Long.parseLong(taskID))
                    .findFirst()
                    .orElse(null);

            if (task == null) {
                responseText(exchange, HttpStatus.NOT_FOUND.code(), HttpStatus.NOT_FOUND.toString());
                return;
            }

            String newTitle = toTask(body).getTitle();
            task.setTitle(newTitle);

            responseJSON(exchange, HttpStatus.OK.code(), taskToJSON(task));
        }

        if (method.equals(HttpMethod.DELETE)) {
            Task task = tasks.stream()
                    .filter(t -> t.getId() == Long.parseLong(taskID))
                    .findFirst()
                    .orElse(null);

            if (task == null) {
                responseText(exchange, HttpStatus.NOT_FOUND.code(), HttpStatus.NOT_FOUND.toString());
                return;
            }

            if (tasks.remove(task)) {
                responseText(exchange, HttpStatus.NO_CONTENT.code(), HttpStatus.NO_CONTENT.toString());
                return;
            }

            responseText(exchange, HttpStatus.INTERNAL_SERVER_ERROR.code(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
        }
    }

    private String readRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream, Charset.forName("EUC-KR")))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    private String tasksToJSON(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private void responseText(HttpExchange exchange, int code, String content) throws IOException {
        final String contentType = "text/html";
        final String charset = "charset=" + StandardCharsets.UTF_8.name();
        exchange.getResponseHeaders().set("Content-type", String.join("; ", contentType, charset));

        exchange.sendResponseHeaders(code, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();

        outputStream.close();
    }

    private void responseJSON(HttpExchange exchange, int code, String content) throws IOException {
        final String contentType = "application/json";
        final String charset = "charset=" + StandardCharsets.UTF_8.name();
        exchange.getResponseHeaders().set("Content-type", String.join("; ", contentType, charset));

        exchange.sendResponseHeaders(code, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();

        outputStream.close();
    }
}
