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

public class DemoHandler implements HttpHandler {

    ObjectMapper objectMapper = new ObjectMapper();
    List<Task> tasks = new ArrayList<>();


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String content = "test";

        String requestMethod = exchange.getRequestMethod();

        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] split = path.split("/");

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        if (split.length >= 2 && split[1].equals("tasks")) {
            if (split.length == 2 && requestMethod.equals("GET")) {
                if (tasks.size() == 0) {
                    content = "[]";
                } else {
                    taskToJson();
                }

            } else if (requestMethod.equals("POST")) {

                if (!body.isBlank()) {
                    Task task = toTask(body);
                    tasks.add(task);
                }
                content = taskToJson();
            }
        }


        exchange.sendResponseHeaders(200, content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task toTask(String body) throws JsonProcessingException {
        Task task = objectMapper.readValue(body, Task.class);
        if (tasks.size() == 0) {
            task.setId(1);
            return task;
        }
        Task laskTask = tasks.get(tasks.size() - 1);
        task.setId(laskTask.getId() + 1);
        return task;
    }

    private String taskToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }
}
