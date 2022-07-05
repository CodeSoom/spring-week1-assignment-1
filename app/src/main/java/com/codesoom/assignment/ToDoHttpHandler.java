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
import java.util.Optional;
import java.util.stream.Collectors;

public class ToDoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long lastId = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        if (method == null) {
            exchange.sendResponseHeaders(400, 0);
            return;
        }

        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();
        if (path == null) {
            exchange.sendResponseHeaders(400, 0);
            return;
        }

        System.out.println(method + " " + path);

        final String body = getRequestBody(exchange).orElse("");
        if (!body.isBlank()) {
            System.out.println(body);
        }

        String content = "";
        int responseCode = 404;

        if (method.equals("GET") && path.equals("/tasks")) {
            try {
                content = tasksToJSON();
                responseCode = 200;
            } catch (IOException e) {
                content = "Failed to convert tasks to JSON";
                responseCode = 500;
            }
        } else if (method.equals("POST") && path.equals("/tasks") && !body.isBlank()) {
            try {
                Task task = toTask(body);
                tasks.add(task);
                content = toString(task);
                responseCode = 201;
            } catch (JsonProcessingException e) {
                content = "Failed to convert request body to Task";
                responseCode = 400;
            } catch (IOException e) {
                content = "Failed to convert Task to string";
                responseCode = 500;
            }
        }

        exchange.sendResponseHeaders(responseCode, content.getBytes().length);

        final OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Optional<String> getRequestBody(HttpExchange exchange) {
        final InputStream inputStream = exchange.getRequestBody();
        final String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        return Optional.ofNullable(body);
    }

    private Task toTask(String content) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        task.setId(lastId++);
        return task;
    }

    private String toString(Task task) throws IOException {
        return objectMapper.writeValueAsString(task);
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
