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
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AssignmentHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks = new ArrayList<>();

//    public AssignmentHttpHandler() {
//        Task task = new Task();
//        task.setId(1L);
//        task.setTitle("Do nothing...");
//
//        tasks.add(task);
//    }

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
        // body 없는 경우 넘어가기
        if (!body.isBlank()) {
            // test object 확인
            Task task = toTask(body);
            System.out.println(task);
        }

        String content = "Hello, world!";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJson();
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task.";
        }

        if (method.equals("PUT") && path.equals("/tasks")) {
            content = tasksToJson();
        }

        if (method.equals("DELETE") && path.equals("/tasks")) {
            content = tasksToJson();
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
