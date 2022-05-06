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

    private static final int HTTP_OK = 200;
    private static final int HTTP_CREATED = 201;
    private static final int HTTP_NO_CONTENT = 204;
    private static final int HTTP_NOT_FOUND = 404;

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

        int statusCode = HTTP_OK;
        String content = "[]";

        if (method.equals("GET") && path.startsWith("/tasks")) {
            try {
                int taskId = (!path.substring(6).isEmpty()) ? extractTaskIdFromPath(path) : -1;
                content = getTaskOrTasks(taskId);
            } catch (IndexOutOfBoundsException e) {
                statusCode = HTTP_NOT_FOUND;
            }
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            Task task = toTask(body);
            task.setId(tasks.size() + 1L);
            tasks.add(task);
            content = taskToJSON(task);
            statusCode = HTTP_CREATED;
        }

        if (method.equals("PUT") && path.startsWith("/tasks")) {
            try {
                Task insertedTask = toTask(body);
                int taskId = extractTaskIdFromPath(path);
                Task task = tasks.get(taskId - 1);
                task.setTitle(insertedTask.getTitle());
                content = taskToJSON(task);
            } catch (IndexOutOfBoundsException e) {
                statusCode = HTTP_NOT_FOUND;
            }
        }

        if (method.equals("DELETE") && path.startsWith("/tasks")) {
            try {
                int taskId = extractTaskIdFromPath(path);
                tasks.remove(taskId - 1);
                statusCode = HTTP_NO_CONTENT;
            } catch (IndexOutOfBoundsException e) {
                statusCode = HTTP_NOT_FOUND;
            }
        }

        sendResponse(exchange, content, statusCode);
    }

    private String getTaskOrTasks(int taskId) throws IOException {
        if (taskId < 0) {
            return tasksToJSON();
        }
        return taskToJSON(tasks.get(taskId - 1));
    }

    private void sendResponse(HttpExchange exchange, String content, int statusCode) throws IOException {
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

    private int extractTaskIdFromPath(String path) {
        return Integer.parseInt(path.substring(7));
    }
}
