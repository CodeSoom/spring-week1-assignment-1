package com.codesoom.assignment;

import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoRestApiHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI url = exchange.getRequestURI();
        String path = url.getPath();

        String content = "Todo List";

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if(method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
        }

        if(method.equals("GET") && path.contains("/tasks/")) {
            String[] splitPath = path.split("/");
            String id = splitPath[splitPath.length - 1];

            for (Task task : tasks) {
                if (task.getId().toString().equals(id)) {
                    content = taskToJSON(task);
                }
            }
        }

        if(method.equals("POST") && path.equals("/tasks")) {
            if(!body.isBlank()) {
                Task task = toTask(body);
                task.setId((long) tasks.size() + 1);
                tasks.add(task);
                content = taskToJSON(task);
            }
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();

        outputStream.close();
    }

    private String tasksToJSON() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String taskToJSON(Task task) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }
}
