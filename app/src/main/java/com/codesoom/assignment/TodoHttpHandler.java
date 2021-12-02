package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Long id = 1L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        Long taskId = parseTaskId(uri);

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        String content = TasksToJSON();

        if(method.equals("GET") && taskId != null) {
            Task task = findTaskById(taskId);
            content = TaskToJSON(task);
        } else if(method.equals("POST") && !body.isBlank()) {
            Task task = toTask(body);
            task.setId(id++);
            tasks.add(task);
            content = TaskToJSON(task);
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.close();

        System.out.println(method + " " + path + " " + taskId);
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String TaskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String TasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private Long parseTaskId(URI uri) {
        String path = uri.getPath();
        String pattern = "^\\/tasks\\/\\d+$";

        if(!Pattern.matches(pattern, path)) {
            return null;
        }

        try {
            Long id = Long.parseLong(path.substring(path.lastIndexOf('/') + 1));
            return id;
        } catch (Exception e) {
            return null;
        }
    }

    private Task findTaskById(Long id) {
        for(Task task : tasks) {
            if(task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }
}
