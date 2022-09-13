package com.codesoom.assignment;

import com.codesoom.assignment.controller.TodoHttpController;
import com.codesoom.assignment.models.HttpStatus;
import com.codesoom.assignment.models.Todo;
import com.codesoom.assignment.validate.TodoValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private final TodoHttpController todoHttpController;
    private final TodoValidator todoValidator;
    public TodoHttpHandler() {
        this.todoHttpController = new TodoHttpController();
        this.todoValidator = new TodoValidator();
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestMethod = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath().substring(1);
            OutputStream outputStream = new ByteArrayOutputStream();
            InputStream requestBody = exchange.getRequestBody();
            OutputStream responseBody = exchange.getResponseBody();

            isContainsInvalidId(exchange, objectMapper, path, outputStream);

            getTodos(exchange, objectMapper, requestMethod, path, outputStream, responseBody);
            if (getTodo(exchange, objectMapper, requestMethod, path, outputStream, requestBody, responseBody)) {
                return;
            }
            if (insert(exchange, objectMapper, requestMethod, path, requestBody, responseBody)) {
                return;
            }
            if (update(exchange, objectMapper, requestMethod, path, outputStream, requestBody, responseBody)) {
                return;
            }
            if (delete(exchange, objectMapper, requestMethod, path, outputStream, requestBody, responseBody)) {
                return;
            }

            closeAll(outputStream, requestBody, responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean delete(HttpExchange exchange, ObjectMapper objectMapper, String requestMethod, String path, OutputStream outputStream, InputStream requestBody, OutputStream responseBody) throws IOException {
        if (isDelete(requestMethod, path)) {
            final String id = path.split("/")[1];
            if (!todoHttpController.isExist(id)) {
                objectMapper.writeValue(outputStream, Arrays.asList());
                exchange.sendResponseHeaders(HttpStatus.NOTFOUND.code(), 0);
                closeAll(outputStream, requestBody, responseBody);
                return true;
            }
            todoHttpController.delete(id);
            exchange.sendResponseHeaders(HttpStatus.NOCONTENT.code(), 0);
        }
        return false;
    }

    private boolean update(HttpExchange exchange, ObjectMapper objectMapper, String requestMethod, String path, OutputStream outputStream, InputStream requestBody, OutputStream responseBody) throws IOException {
        if (isUpdate(requestMethod, path)) {
            final String id = path.split("/")[1];

            final String content = new BufferedReader(new InputStreamReader(requestBody))
                    .lines()
                    .collect(Collectors.joining("\n"));
            if (content.isBlank()) {
                return true;
            }
            Todo body = objectMapper.readValue(content, Todo.class);
            body.setId(Integer.parseInt(id));

            if (!todoHttpController.isExist(id)) {
                objectMapper.writeValue(outputStream, Arrays.asList());
                exchange.sendResponseHeaders(HttpStatus.NOTFOUND.code(), 0);

                closeAll(outputStream, requestBody, responseBody);
                return true;
            }

            Todo updated = todoHttpController.update(body);
            exchange.sendResponseHeaders(HttpStatus.SUCCESS.code(), updated.toString().getBytes().length);
            responseBody.write(updated.toString().getBytes());
        }
        return false;
    }

    private boolean insert(HttpExchange exchange, ObjectMapper objectMapper, String requestMethod, String path, InputStream requestBody, OutputStream responseBody) throws IOException {
        if (isInsert(requestMethod, path)) {
            String content = new BufferedReader(new InputStreamReader(requestBody))
                    .lines()
                    .collect(Collectors.joining("\n"));
            if (content.isBlank()) {
                return true;
            }
            Todo inserted = todoHttpController.insert(objectMapper.readValue(content, Todo.class));
            exchange.sendResponseHeaders(HttpStatus.CREATED.code(), inserted.toString().getBytes().length);
            responseBody.write(inserted.toString().getBytes());
        }
        return false;
    }

    private boolean getTodo(HttpExchange exchange, ObjectMapper objectMapper, String requestMethod, String path, OutputStream outputStream, InputStream requestBody, OutputStream responseBody) throws IOException {
        if (isGetTodo(requestMethod, path)) {
            final String id = path.split("/")[1];

            if (todoHttpController.isEmpty() || !todoHttpController.isExist(id)) {
                objectMapper.writeValue(outputStream, Arrays.asList());
                exchange.sendResponseHeaders(HttpStatus.NOTFOUND.code(), outputStream.toString().getBytes().length);
                responseBody.write(outputStream.toString().getBytes());

                closeAll(outputStream, requestBody, responseBody);
                return true;
            }

            objectMapper.writeValue(outputStream,todoHttpController.getTodos(id));
            exchange.sendResponseHeaders(HttpStatus.SUCCESS.code(), outputStream.toString().getBytes().length);
            responseBody.write(outputStream.toString().getBytes());
        }
        return false;
    }

    private void getTodos(HttpExchange exchange, ObjectMapper objectMapper, String requestMethod, String path, OutputStream outputStream, OutputStream responseBody) throws IOException {
        if (isGetTodos(requestMethod, path)) {
            objectMapper.writeValue(outputStream, todoHttpController.getTodos());
            exchange.sendResponseHeaders(HttpStatus.SUCCESS.code(), outputStream.toString().getBytes().length);
            responseBody.write(outputStream.toString().getBytes());
        }
    }

    private void isContainsInvalidId(HttpExchange exchange, ObjectMapper objectMapper, String path, OutputStream outputStream) throws IOException {
        try {
            todoValidator.isContainsInvalidId(path);
        } catch (IllegalArgumentException e) {
            objectMapper.writeValue(outputStream, Arrays.asList());
            exchange.sendResponseHeaders(HttpStatus.NOTFOUND.code(), outputStream.toString().getBytes().length);
            e.printStackTrace();
        }
    }

    private void closeAll(OutputStream outputStream, InputStream requestBody, OutputStream responseBody) throws IOException {
        if (requestBody != null) {
            requestBody.close();
        }
        if (responseBody != null) {
            responseBody.flush();
            responseBody.close();
        }
        if (outputStream != null ) {
            outputStream.flush();
            outputStream.close();
        }
    }

    private boolean isGetTodos(String requestMethod, String path) {
        return "GET".equals(requestMethod) &&
                path.equals("tasks") &&
                path.split("/").length == 1;
    }

    private boolean isGetTodo(String requestMethod, String path) {
        return "GET".equals(requestMethod) &&
                path.contains("tasks") &&
                path.split("/").length > 1;
    }

    private boolean isInsert(String requestMethod, String path) {
        return "POST".equals(requestMethod) && path.equals("tasks");
    }

    private boolean isUpdate(String requestMethod, String path) {
        return ("PUT".equals(requestMethod) || "PATCH".equals(requestMethod)) && path.contains("tasks");
    }

    private boolean isDelete(String requestMethod, String path) {
        return "DELETE".equals(requestMethod) && path.contains("tasks");
    }
}
