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

        if(path.equals("/tasks")) {
            handleCollection(exchange, method);
            return;
        }

        if (method.equals("GET") && taskId != null) {
            Task task = findTaskById(taskId);
            send(exchange, 200, toJSON(task));
        } else if(method.equals("DELETE") && taskId != null) {
            Task task = findTaskById(taskId);
            tasks.remove(task);
            send(exchange, 200, "");
        } else if(method.equals("PUT") && taskId != null) {
            String body = getBody(exchange);

            Task updateTask = toTask(body);
            Task originalTask = findTaskById(taskId);

            updateTask.setId(originalTask.getId());
            tasks.set(tasks.indexOf(originalTask), updateTask);

            send(exchange, 200, toJSON(updateTask));
        }

        System.out.println(method + " " + path + " " + taskId);
    }

    private void handleCollection(HttpExchange exchange, String method) throws IOException {
        if(method.equals("GET")) {
            send(exchange, 200, toJSON(tasks));
        }

        if(method.equals("POST")) {
            String body = getBody(exchange);

            Task task = toTask(body);
            task.setId(id++);
            tasks.add(task);

            send(exchange, 200, toJSON(task));
        }
    }

    private String getBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
    }

    private void send(HttpExchange exchange, int statusCode, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String toJSON(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);

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
