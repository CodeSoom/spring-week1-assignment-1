package com.codesoom.assignment;

import com.codesoom.assignment.controller.TodoHttpController;
import com.codesoom.assignment.models.Todo;
import com.codesoom.assignment.validate.TodoValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
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
                exchange.sendResponseHeaders(404, outputStream.toString().getBytes().length);
                e.printStackTrace();
            }

            if (requestMethod.equals("GET") &&
                    exchange.getRequestURI().getPath().substring(1).equals("tasks") &&
                    exchange.getRequestURI().getPath().substring(1).split("/").length == 1) {

                objectMapper.writeValue(outputStream, todoHttpController.getTodo());

                exchange.sendResponseHeaders(200, outputStream.toString().getBytes().length);
                responseBody.write(outputStream.toString().getBytes());
            }

            if (requestMethod.equals("GET") &&
                    path.contains("tasks") &&
                    path.split("/").length > 1) {
                String id = path.split("/")[1];

                int status = 200;
                if (todoHttpController.isEmpty() || !todoHttpController.isExist(id)) {
                    objectMapper.writeValue(outputStream, Arrays.asList());
                    status = 404;
                } else {
                    objectMapper.writeValue(outputStream,todoHttpController.getTodo(id));
                }
                exchange.sendResponseHeaders(status, outputStream.toString().getBytes().length);
                responseBody.write(outputStream.toString().getBytes());
            }


            if (requestMethod.equals("POST") && path.equals("tasks")) {
                String content = new BufferedReader(new InputStreamReader(requestBody))
                        .lines()
                        .collect(Collectors.joining("\n"));
                if (!content.isBlank()) {
                    Todo inserted = todoHttpController.insert(objectMapper.readValue(content, Todo.class));
                    exchange.sendResponseHeaders(201, inserted.toString().getBytes().length);
                    responseBody.write(inserted.toString().getBytes());
                }
            }

            if ((requestMethod.equals("PUT") || requestMethod.equals("PATCH")) && path.contains("tasks")) {
                String id = path.split("/")[1];

                int status = 200;
                String content = new BufferedReader(new InputStreamReader(requestBody))
                        .lines()
                        .collect(Collectors.joining("\n"));
                if (!content.isBlank()) {
                    Todo body = objectMapper.readValue(content, Todo.class);
                    body.setId(Integer.parseInt(id));

                    if (!todoHttpController.isExist(id)) {
                        objectMapper.writeValue(outputStream, Arrays.asList());
                        status = 404;
                        exchange.sendResponseHeaders(status, 0);
                    } else {
                        Todo updated = todoHttpController.update(body);
                        exchange.sendResponseHeaders(status, updated.toString().getBytes().length);
                        responseBody.write(updated.toString().getBytes());
                    }
                }
            }

            if (requestMethod.equals("DELETE") && path.contains("tasks")) {
                String id = path.split("/")[1];
                int status = 204;
                if (!todoHttpController.isExist(id)) {
                    objectMapper.writeValue(outputStream, Arrays.asList());
                    status = 404;
                    exchange.sendResponseHeaders(status, 0);
                } else {
                    todoHttpController.delete(id);
                    exchange.sendResponseHeaders(status, 0);
                }
            }

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
