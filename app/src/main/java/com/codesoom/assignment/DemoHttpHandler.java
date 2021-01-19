package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private TaskRepository taskRepository = new TaskRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String content = "[]";
        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
            exchange.sendResponseHeaders(200, content.getBytes().length);
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            Task task = taskRepository.save(toTask(body));
            content = taskToJSON(task);
            exchange.sendResponseHeaders(201, content.getBytes().length);
        }

        if (method.equals("PUT") && path.startsWith("/tasks")) {
            String[] identities = path.split("\\/");
            Long id = Long.valueOf(identities[2]);
            Task task = taskRepository.update(id, toTask(body));
            content = taskToJSON(task);
            exchange.sendResponseHeaders(200, content.getBytes().length);
        }

        if (method.equals("DELETE") && path.startsWith("/tasks")) {
            String[] identities = path.split("\\/");
            Long id = Long.valueOf(identities[2]);
            taskRepository.delete(id);
            content = "";
            exchange.sendResponseHeaders(200, content.getBytes().length);
        }

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String taskToJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskRepository.findAll());

        return outputStream.toString();
    }

}
