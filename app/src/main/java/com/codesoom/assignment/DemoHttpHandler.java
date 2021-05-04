package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();
    private Long id = 0L;

    public DemoHttpHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        System.out.println(
                String.format("%s %s Something requests...",
                        method,
                        path
                )
        );

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader((inputStream)))
                .lines()
                .collect(Collectors.joining("\n"));

        String content = "RESPONSE";

        if ("GET".equals(method) && "/tasks".equals(path)) {
            content = tasksToJson();
        }

        if ("POST".equals(method) && "/tasks".equals(path)) {
            content = "CREATE A NEW TASK";

            if (!body.isBlank()) {
                Task task = JsonToTask(body);
                task.setId(id + 1);
                tasks.add(task);
            }
        }

        byte[] responseBytes = content.getBytes();

        exchange.sendResponseHeaders(200, responseBytes.length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
    }

    private Task JsonToTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson() throws IOException {
        if (tasks.size() == 0) return "[]";

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
