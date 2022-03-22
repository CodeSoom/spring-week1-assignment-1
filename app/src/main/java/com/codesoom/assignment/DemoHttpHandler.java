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

public class DemoHttpHandler implements HttpHandler {
    private  ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks = new ArrayList<>();


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // REST - CRUD
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader((inputStream)))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);

        String content = "Hello, World!";

        if(method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();

            httpExchange.sendResponseHeaders(200, content.getBytes().length);
        }

        if(method.equals("GET") && path.contains("/tasks/")
                && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            Integer number = Integer.valueOf(numberString) - 1;

            content = getTaskToJSON(number);
            httpExchange.sendResponseHeaders(200, content.getBytes().length);
        }

        if(method.equals("DELETE") && path.contains("/tasks/")
                && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            Integer number = Integer.valueOf(numberString) - 1;

            tasks.remove(number);
            httpExchange.sendResponseHeaders(200, content.getBytes().length);
        }

        if(!body.isEmpty() && method.equals("POST") && path.equals("/tasks")) {
            Task task = toTask(body);
            task.setId(tasks.size() + 1L);
            tasks.add(task);

            content = latestTaskToJSON();
            httpExchange.sendResponseHeaders(201, content.getBytes().length);
        }

        if(!body.isEmpty() && method.equals("PUT") && path.contains("/tasks/")
        && path.lastIndexOf("/") != path.length() - 1) {
            String numberString = path.substring(path.lastIndexOf("/") + 1);
            Integer number = Integer.valueOf(numberString) - 1;
            Task task = toTask(body);
            task.setId(number + 1L);

            tasks.set(number, task);


            content = getTaskToJSON(number);

            httpExchange.sendResponseHeaders(200, content.getBytes().length);
        }

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String latestTaskToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks.get(tasks.size() - 1));

        return outputStream.toString();
    }

    private String getTaskToJSON(int index) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks.get(index));

        return outputStream.toString();
    }
}