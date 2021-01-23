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

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        System.out.println(method + " " + path);

        if(path.equals("/tasks")) {
            String content = handleCollection(exchange, method);
            send(exchange,200, content);
            return;
        }

        send(exchange, 200, "나는 진정 행복한 부자가 될 것이다.");
    }

    private void send(HttpExchange exchange, int statusCode, String content) throws IOException {

        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private String handleCollection(HttpExchange exchange, String method) throws IOException {
        if(method.equals("GET")) {
            return tasksToJSON();
        }

        if(method.equals("POST")) {
            InputStream requestBody = exchange.getRequestBody();
            String body = new BufferedReader(new InputStreamReader(requestBody))
                    .lines()
                    .collect(Collectors.joining("\n"));

            Task task = toTask(body);
            tasks.add(task);

            System.out.println(body);

            return "A task has been created";
        }

        return "e: invalid method";
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
