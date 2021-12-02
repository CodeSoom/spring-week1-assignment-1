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
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {
    List<Task> tasks = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    public MyHttpHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.toString();
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " - " + path);
        if (!requestBody.isBlank()) {
            System.out.println(requestBody);
        }


        String content = "";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJson();
            exchange.sendResponseHeaders(200, content.getBytes().length);
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            Task task = toTask(requestBody);

            long newId = tasks.size() == 0 ? 1 : tasks.get(tasks.size() - 1).getId() + 1;
            task.setId(newId);
            tasks.add(task);

            exchange.sendResponseHeaders(201, content.getBytes().length);
        }


        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.close();
    }

    private Task toTask(String requestBody) throws JsonProcessingException {
        return objectMapper.readValue(requestBody, Task.class);
    }

    private String tasksToJson() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}