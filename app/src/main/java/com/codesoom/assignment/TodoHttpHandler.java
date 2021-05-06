package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {

    private final Long taskIdSequence = 0L;

    private List<Task> taskList = new ArrayList<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();

        InputStream inputStream = httpExchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String content = "";

        if (requestMethod.equals("GET") && path.equals("/tasks")) {
            content = toJson(taskList);
            httpExchange.sendResponseHeaders(200, content.getBytes().length);
        }

        if (requestMethod.equals("POST") && path.equals("/tasks")) {
            Task task = JsonToTask(requestBody);
            task.setId(taskIdSequence + 1L);
            content = toJson(task);
            httpExchange.sendResponseHeaders(201, content.getBytes().length);
        }

        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private Task JsonToTask(String requestBody) throws JsonProcessingException {
        return objectMapper.readValue(requestBody, Task.class);
    }

    private String toJson(Object taskObject) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskObject);

        return outputStream.toString();
    }
}
