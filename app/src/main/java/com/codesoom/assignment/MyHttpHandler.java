package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {
    private Tasks tasks;
    private Pattern pattern;
    private JsonConverter jsonConverter;
    private IdGenerator idGenerator;

    public MyHttpHandler() {
        this.tasks = new Tasks();
        this.pattern = Pattern.compile("/tasks/\\d+");
        this.jsonConverter = new JsonConverter();
        this.idGenerator = new IdGenerator();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        System.out.println(method + " " + path);

        if (path.equals("/")) {
            handleHealthCheck(exchange, method);
        }
        if (path.equals("/tasks")) {
            handleCollection(exchange, method);
            return;
        }
        if (path.startsWith("/tasks") && pattern.matcher(path).matches()) {
            handleItem(exchange, path);
        }
    }

    private void handleCollection(HttpExchange exchange, String method) throws IOException {
        if (method.equals("GET")) {
            handleList(exchange);
        }
        if (method.equals("POST")) {
            handleCreate(exchange);
        }
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        String body = getBody(exchange);
        if (Objects.isNull(body)) {
            exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getCode(), 0);
            return;
        }
        Task task = jsonConverter.toTask(body);
        task.setId(idGenerator.generate());
        tasks.addTask(task);
        handleResponse(exchange, 201, jsonConverter.taskToJson(task));
    }

    private void handleHealthCheck(HttpExchange exchange, String method) throws IOException {
        if (method.equals("HEAD")) {
            exchange.sendResponseHeaders(HttpStatus.OK.getCode(), 0);
            return;
        }
    }

    private void handleList(HttpExchange exchange) throws IOException {
        String content = jsonConverter.tasksToJSON(tasks);
        handleResponse(exchange, 200, content);
    }

    private void handleResponse(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void handleItem(HttpExchange exchange, String path) throws IOException {
        String method = exchange.getRequestMethod();
        String body = getBody(exchange);

        Task task = findTask(path);
        if (task == null) {
            handleResponse(exchange, HttpStatus.NOT_FOUND.getCode(), "");
            return;
        }
        if (method.equals("GET")) {
            handleDetail(exchange, task);
            return;
        }
        if (method.equals("PUT") || method.equals("PATCH")) {
            handlePut(exchange, body, task);
            return;
        }
        if (method.equals("DELETE")) {
            handleDelete(exchange, task);
        }
    }

    private void handleDetail(HttpExchange exchange, Task task) throws IOException {
        handleResponse(exchange, HttpStatus.OK.getCode(), jsonConverter.taskToJson(task));
    }

    private void handlePut(HttpExchange exchange, String body, Task task) throws IOException {
        if (Objects.isNull(body)) {
            handleResponse(exchange, HttpStatus.BAD_REQUEST.getCode(), "");
        }
        task.setTitle(jsonConverter.extractTitle(body));
        handleResponse(exchange, HttpStatus.OK.getCode(), jsonConverter.taskToJson(task));
    }

    private void handleDelete(HttpExchange exchange, Task task) throws IOException {
        tasks.remove(task);
        handleResponse(exchange, HttpStatus.NO_CONTENT.getCode(), "");
    }

    private Task findTask(String path) {
        Long pathVariable = extractPathVariable(path);
        return tasks.findTask(pathVariable).orElse(null);
    }

    private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Long extractPathVariable(String path) {
        return Long.parseUnsignedLong(path.substring(path.lastIndexOf("/") + 1));
    }
}
