package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();

    public DemoHttpHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Nothing");
        tasks.add(task);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();

        String path = uri.getPath();

        InputStream requestBody = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(requestBody))
                .lines()
                .collect(Collectors.joining("\n"));
        String content = "Hello, World";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJson();
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = "Created";
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private String tasksToJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return objectMapper.toString();
    }
}
