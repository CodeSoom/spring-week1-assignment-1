package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private Pattern pattern = Pattern.compile("/tasks/([^/]+)");

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
        String content = "Hello, world";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
            exchange.sendResponseHeaders(200, content.getBytes().length);
        }

        if (method.equals("GET") && pattern.matcher(path).matches()) {
            Long pathVariable = extractPathVariable(path);

            Optional<Task> foundTask = findTask(pathVariable);
            if (foundTask.isPresent()) {
                content = taskToJson(foundTask.get());
                exchange.sendResponseHeaders(200, content.getBytes().length);
            } else {
                System.out.println("not found task" + foundTask);
                exchange.sendResponseHeaders(400, 0);
            }
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = "create a new task";
            if (!body.isBlank()) {
                Task task = toTask(body);
                task.setId(IdGenerator.generate());
                tasks.add(task);
                exchange.sendResponseHeaders(201, content.getBytes().length);
            }
        }

        if (method.equals("PUT") && pattern.matcher(path).matches()) {
            content = "update a task";
            Long pathVariable = extractPathVariable(path);

            if (!body.isBlank()) {
                Optional<Task> foundTask = findTask(pathVariable);
                if (foundTask.isPresent()) {
                    foundTask.get().setTitle(extractValue(body));
                    exchange.sendResponseHeaders(200, content.getBytes().length);
                } else {
                    exchange.sendResponseHeaders(400, 0);
                }
            }
        }

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private Long extractPathVariable(String path) {
        String[] paths = path.split("/");
        return Long.parseUnsignedLong(paths[2]);
    }

    private Optional<Task> findTask(Long pathVariable) {
        return tasks.stream()
                .filter(t -> t.getId() == pathVariable)
                .findFirst();
    }

    private String extractValue(String content) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        return task.getTitle();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String taskToJson(Task task) throws IOException {
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
