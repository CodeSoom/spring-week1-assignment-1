package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppHttpHandler implements HttpHandler {
    List<Task> tasks = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    public AppHttpHandler() {
        // Test용
        tasks.add(new Task(1L, "과제 제출하기1"));
        tasks.add(new Task(222L, "과제 제출하기2"));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String content = "No Content";
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        Long id;

        if (path.length() > 6) {
            id = Long.parseLong(path.substring(path.indexOf("/", 1) + 1));
        } else {
            id = 0L;
        }

        if (method.equals("GET") && path.contains("/tasks")) {
            if (hasUserId(id)) {
                content = tasksToJson(tasks);
            } else {
                Optional<Task> task = tasks.stream()
                        .filter(s -> s.getId().equals(id))
                        .findFirst();

                if (task.isPresent()) {
                    content = taskToJson(task);
                }
            }
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private boolean hasUserId(Long id) {
        return id == 0;
    }

    private String tasksToJson(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private String taskToJson(Optional<Task> task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task.get());
        return outputStream.toString();
    }
}
