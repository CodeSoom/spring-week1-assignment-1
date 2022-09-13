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

            try {
                todoValidator.isContainsInvalidId(path);
            } catch (IllegalArgumentException e) {
                objectMapper.writeValue(outputStream, Arrays.asList());
                exchange.sendResponseHeaders(HttpStatus.NOTFOUND.code(), outputStream.toString().getBytes().length);
                e.printStackTrace();
            }

            if (isGetTodos(requestMethod, path)) {

                objectMapper.writeValue(outputStream, todoHttpController.getTodos());

                exchange.sendResponseHeaders(HttpStatus.SUCCESS.code(), outputStream.toString().getBytes().length);
                responseBody.write(outputStream.toString().getBytes());
            }

            if (isGetTodo(requestMethod, path)) {
                String id = path.split("/")[1];

                if (todoHttpController.isEmpty() || !todoHttpController.isExist(id)) {
                    objectMapper.writeValue(outputStream, Arrays.asList());
                    exchange.sendResponseHeaders(HttpStatus.NOTFOUND.code(), outputStream.toString().getBytes().length);
                    responseBody.write(outputStream.toString().getBytes());

                    close(outputStream, requestBody, responseBody);
                    return;
                }

                objectMapper.writeValue(outputStream,todoHttpController.getTodos(id));
                exchange.sendResponseHeaders(HttpStatus.SUCCESS.code(), outputStream.toString().getBytes().length);
                responseBody.write(outputStream.toString().getBytes());
            }


            if (isInsert(requestMethod, path)) {
                String content = new BufferedReader(new InputStreamReader(requestBody))
                        .lines()
                        .collect(Collectors.joining("\n"));
                if (content.isBlank()) return;
                Todo inserted = todoHttpController.insert(objectMapper.readValue(content, Todo.class));
                exchange.sendResponseHeaders(HttpStatus.CREATED.code(), inserted.toString().getBytes().length);
                responseBody.write(inserted.toString().getBytes());
            }

            if (isUpdate(requestMethod, path)) {
                String id = path.split("/")[1];

                String content = new BufferedReader(new InputStreamReader(requestBody))
                        .lines()
                        .collect(Collectors.joining("\n"));
                if (content.isBlank()) return;
                Todo body = objectMapper.readValue(content, Todo.class);
                body.setId(Integer.parseInt(id));

                if (!todoHttpController.isExist(id)) {
                    objectMapper.writeValue(outputStream, Arrays.asList());
                    exchange.sendResponseHeaders(HttpStatus.NOTFOUND.code(), 0);

                    close(outputStream, requestBody, responseBody);
                    return;
                }

                Todo updated = todoHttpController.update(body);
                exchange.sendResponseHeaders(HttpStatus.SUCCESS.code(), updated.toString().getBytes().length);
                responseBody.write(updated.toString().getBytes());
            }

            if (isDelete(requestMethod, path)) {
                String id = path.split("/")[1];
                if (!todoHttpController.isExist(id)) {
                    objectMapper.writeValue(outputStream, Arrays.asList());
                    exchange.sendResponseHeaders(HttpStatus.NOTFOUND.code(), 0);
                    close(outputStream, requestBody, responseBody);
                    return;
                }
                todoHttpController.delete(id);
                exchange.sendResponseHeaders(HttpStatus.NOCONTENT.code(), 0);
            }

            close(outputStream, requestBody, responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void close(OutputStream outputStream, InputStream requestBody, OutputStream responseBody) throws IOException {
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
