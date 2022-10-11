package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.utils.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppHttpHandler implements HttpHandler {
    static Long id = 0L;
    List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String content = "No Content";
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        Long userId;
        OutputStream responseBody = exchange.getResponseBody();

        String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining("\n"));

        if (path.length() > 6) {
            userId = Long.parseLong(path.substring(path.indexOf("/", 1) + 1));
        } else {
            userId = 0L;
        }

        if (!path.contains("/tasks")) {
            exchange.sendResponseHeaders(404, 0);
            responseBody.close();
            return;
        }

        if (method.equals("PUT") || method.equals("PATCH") || method.equals("DELETE")) {
            if (isEmptyUserId(userId)) {
                exchange.sendResponseHeaders(400, 0);
                responseBody.close();
                return;
            }
        }

        if (method.equals("POST") || method.equals("PUT") || method.equals("PATCH")) {
            if (requestBody.isBlank()) {
                exchange.sendResponseHeaders(400, 0);
                responseBody.close();
                return;
            }
        }

        switch (method) {
            case "GET":
                if (isEmptyUserId(userId)) {
                    content = JsonUtil.writeValue(tasks);
                } else {
                    Optional<Task> task = tasks.stream()
                            .filter(t -> t.getId().equals(userId))
                            .findFirst();

                    if (task.isPresent()) {
                        content = JsonUtil.writeValue(task.get());
                    }
                }

                exchange.sendResponseHeaders(200, content.getBytes().length);
                break;
            case "POST":
                Task task = JsonUtil.readValue(requestBody, Task.class);
                task.setId(++id);
                tasks.add(task);

                content = JsonUtil.writeValue(task);
                exchange.sendResponseHeaders(201, content.getBytes().length);
                break;
            case "PUT":
            case "PATCH":
                String newTitle = JsonUtil.readValue(requestBody, Task.class).getTitle();

                Optional<Task> findTask = tasks.stream()
                        .filter(t -> t.getId().equals(userId))
                        .findFirst();

                if (findTask.isPresent()) {
                    int indexOfOriginTask = tasks.indexOf(findTask.get());
                    Task originTask = tasks.get(indexOfOriginTask);
                    originTask.setTitle(newTitle);
                    content = JsonUtil.writeValue(originTask);
                }

                exchange.sendResponseHeaders(200, content.getBytes().length);
                break;
            case "DELETE":

                Optional<Task> findTask2 = tasks.stream()
                        .filter(t -> t.getId().equals(userId))
                        .findFirst();

                if (findTask2.isPresent()) {
                    int indexOfOriginTask = tasks.indexOf(findTask2.get());
                    tasks.remove(indexOfOriginTask);
                }

                content = "";
                exchange.sendResponseHeaders(200, content.getBytes().length);
                break;
            default:
                exchange.sendResponseHeaders(400, 0);
                responseBody.close();
                return;
        }


        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private boolean isEmptyUserId(Long id) {
        return id.equals(0L);
    }
}
