package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);

        int statusCode = 200;
        String content = "[]";

        if (method.equals("GET") && path.startsWith("/tasks")) {
            try {
                if (!path.substring(6).isEmpty()) {
                    int taskId = Integer.parseInt(path.substring(7));
                    Task task = tasks.get(taskId - 1);
                    content = taskToJSON(tasks.get(taskId - 1));
                }
                else {
                    content = tasksToJSON();
                }
            } catch (Exception e) {
                statusCode = 404;
            }
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            if (!body.isBlank()) {
                Task task = toTask(body);
                task.setId(tasks.size()+1L);
                tasks.add(task);
                content = taskToJSON(task);
                statusCode = 201;
            } else {
                content = "No value inserted";
                statusCode = 400;
            }
        }

        if (method.equals("PUT") && path.startsWith("/tasks")) {
            try {
                if (path.substring(6).isEmpty()) {
                    content = "No task ID inserted";
                    statusCode = 400;
                } else {
                    Task insertedTask = toTask(body);
                    int taskId = Integer.parseInt(path.substring(7));
                    Task task = tasks.get(taskId - 1);
                    task.setTitle(insertedTask.getTitle());
                    content = taskToJSON(task);
                }
            } catch (Exception e) {
                statusCode = 404;
            }
        }

        if (method.equals("DELETE") && path.startsWith("/tasks")) {
            try {
                if (path.substring(6).isEmpty()) {
                    content = "No task ID inserted";
                    statusCode = 400;
                } else {
                    int taskId = Integer.parseInt(path.substring(7));
                    tasks.remove(taskId - 1);
                    statusCode = 204;
                }
            } catch (Exception e) {
                statusCode = 404;
            }
        }

        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
