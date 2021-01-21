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

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();

    public DemoHttpHandler() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("'나는 이미 행복한 부자다' 라고 되뇌기");

        tasks.add(task1);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream requestBody = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(requestBody))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);
        System.out.println(body);

        String content = "나는 진정 행복한 부자가 될 것이다.";

        if(method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
        }

        if(method.equals("POST") && path.equals("/tasks")) {
            content = "A task has been created";
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();

    }

    private String tasksToJSON() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
