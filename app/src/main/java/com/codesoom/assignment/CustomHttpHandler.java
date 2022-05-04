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

public class CustomHttpHandler implements HttpHandler {

    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        InputStream inputStream = exchange.getRequestBody();

        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        Task task = null;
        if (!requestBody.isBlank()) {
            task = jsonToTask(requestBody);
        }

        System.out.println(task);

        if(hasTaskId(path)) {
            if ("PUT".equals(method) || "PACTH".equals(method)) {
                System.out.println("put/patch");
            } else if ("DELETE".equals(method)) {
                System.out.println("delete");
            } else if ("GET".equals(method)) {
                System.out.println("get");
            }
        } else {
            if ("GET".equals(method)) {
                System.out.println("get");
            } else if ("POST".equals(method)) {
                System.out.println("post");
            }
        }
    }

    private Task jsonToTask(String requestBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(requestBody, Task.class);
    }

    private boolean hasTaskId(String path) {
        return path.startsWith("/tasks/") && path.split("/").length > 2;
    }
}
