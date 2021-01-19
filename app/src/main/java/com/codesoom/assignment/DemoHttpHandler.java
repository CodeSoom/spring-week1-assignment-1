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
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long id = 1L;

    public DemoHttpHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // REST - CRUD
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        // POST             -> status 201
        // PATCH(UPDATE)    -> (tasks/1 title=one)
        // DELETE           -> /tasks/1
        // GET              -> /tasks/1

        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);

        String[] str = path.split("/");
        String content = "Hello World!";
        int statusCode = 200;

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
        }

        if (method.equals("POST") && path.equals("/tasks") && !body.isBlank()) {
            Task task = toTask(body);
            task.setId(id++);
            System.out.println(task.toString());
            tasks.add(task);
            statusCode = 201;
            content = "Create a new task.";
        }

        if (method.equals("PUT") && str[1].equals("tasks") && !body.isBlank()) {
            int lastId = Integer.parseInt(str[str.length - 1]);
            System.out.println(tasks.size());
            if (!(tasks.size() == 0)){
            }

        }



        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();

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
}
