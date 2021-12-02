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

public class MyHttpHandler implements HttpHandler {
    List<Task> tasks = new ArrayList<>();

    public MyHttpHandler() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("test");
        tasks.add(task);
        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("test2");
        tasks.add(task2);
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
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.close();
    }

    private String tasksToJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}