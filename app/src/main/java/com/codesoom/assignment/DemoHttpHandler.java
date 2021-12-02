package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private static Long autoId = 0L;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        String body = new BufferedReader(inputStreamReader)
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);

        if (!body.isBlank()) {
            System.out.println(body);
        }

        String content = null;
        int code = 200;
        long responseLength = 0L;

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
        }

        if (method.equals("GET") && path.contains("/tasks/")) {
            Long id = getId(path);
            content = taskToJSON(id);
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            Task task = toTask(body);
            task.setId(++autoId);
            tasks.add(task);

            content = toJSON(task);
            code = 201;
        }

        if ("PATCH, PUT".contains(method) && path.contains("/tasks/")) {
            Long id = getId(path);
            Task task = updateTask(id, body);
            content = toJSON(task);
        }

        if (method.equals("DELETE") && path.contains("/tasks/")) {
            Long id = getId(path);
            deleteTask(id);
            content = null;
        }


        int responseBodyLength = content != null ? content.getBytes().length : 0;
        byte[] responseBody = content != null ? content.getBytes() : new byte[0];

        exchange.sendResponseHeaders(code, responseBodyLength);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBody);
        outputStream.flush();
        outputStream.close();
    }

    private Long getId(String path) {
        String[] split = path.split("/tasks/");
        return Long.valueOf(split[1]);
    }

    private String toJSON(Task task) {
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            objectMapper.writeValue(outputStream, task);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toString();
    }

    private Task toTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

    private String taskToJSON(Long id) {
        String result = null;

        for (Task task : tasks) {
            if (task.getId() == id) {
                result = toJSON(task);
                break;
            }
        }

        return result;
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private Task updateTask(Long id, String body) throws JsonProcessingException {

        Task findTask = toTask(body);

        Task result = null;

        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setTitle(findTask.getTitle());
                result = task;
                break;
            }
        }

        return result;
    }

    private void deleteTask(Long id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                tasks.remove(task);
                break;
            }
        }
    }
}
