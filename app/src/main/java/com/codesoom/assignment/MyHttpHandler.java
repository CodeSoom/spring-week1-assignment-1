package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
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
        String content = "";
        System.out.println(method + " " + path);

        if (path.equals("/")) {
            handleHealthCheck(exchange, method);
        }

        if (path.equals("/tasks")) {
            handleCollection(exchange, method);
            return;
        }

        if (path.startsWith("/tasks")) {
            content = methodHandler(exchange, path);
        }
        handleResponse(exchange, content);
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
        send(exchange, 201, jsonConverter.taskToJson(task));
    }

    private void handleHealthCheck(HttpExchange exchange, String method) throws IOException {
        if (method.equals("HEAD")) {
            exchange.sendResponseHeaders(HttpStatus.OK.getCode(), 0);
            return;
        }
    }

    private void handleList(HttpExchange exchange) throws IOException {
        String content = jsonConverter.tasksToJSON(tasks);
        send(exchange, 200, content);
    }

    private void handleResponse(HttpExchange exchange, String content) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void send(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String methodHandler(HttpExchange exchange, String path) throws IOException {
        String method = exchange.getRequestMethod();
        String body = getBody(exchange);
        String content = "";

        if (method.equals("GET") && pattern.matcher(path).matches()) {
            Long pathVariable = extractPathVariable(path);
            Optional<Task> task = tasks.findTask(pathVariable);

            if (task.isEmpty()) {
                exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
                return content;
            }
            exchange.sendResponseHeaders(HttpStatus.OK.getCode(), content.getBytes().length);
            return jsonConverter.taskToJson(task.get());
        }

        if (method.equals("PUT") && pattern.matcher(path).matches()) {
            Long pathVariable = extractPathVariable(path);
            if (Objects.isNull(body)) {
                exchange.sendResponseHeaders(HttpStatus.BAD_REQUEST.getCode(), 0);
                return content;
            }
            Optional<Task> task = tasks.findTask(pathVariable);
            if (task.isEmpty()) {
                exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
                return content;
            }
            task.get().setTitle(jsonConverter.extractTitle(body));
            exchange.sendResponseHeaders(HttpStatus.OK.getCode(), content.getBytes().length);
            return jsonConverter.taskToJson(task.get());
        }

        if (method.equals("DELETE") && pattern.matcher(path).matches()) {
            content = "delete a task";
            Long pathVariable = extractPathVariable(path);
            Optional<Task> task = tasks.findTask(pathVariable);

            if (task.isEmpty()) {
                exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
                return content;
            }

            tasks.remove(task.get());
            exchange.sendResponseHeaders(HttpStatus.NO_CONTENT.getCode(), content.getBytes().length);
            return content;
        }

        return content;
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
