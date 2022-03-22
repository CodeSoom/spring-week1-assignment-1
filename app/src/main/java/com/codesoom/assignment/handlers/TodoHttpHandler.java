package com.codesoom.assignment.handlers;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logRequest(httpExchange);

        InputStream inputStream = httpExchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));


        String content = "Hello, world!";
        httpExchange.sendResponseHeaders(200, content.getBytes(StandardCharsets.UTF_8).length);

        OutputStream outputStream = httpExchange.getResponseBody();

        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        logResponse(content);
    }

    private String toJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private void logRequest(HttpExchange httpExchange) {
        String requestMethod = httpExchange.getRequestMethod();
        URI requestURI = httpExchange.getRequestURI();
        String requestPath = requestURI.getPath();

        System.out.println("requestMethod = " + requestMethod);
        System.out.println("requestURI = " + requestURI);
        System.out.println("requestPath = " + requestPath);
        System.out.println();
    }

    private void logResponse(String responseContent) {
        System.out.println("responseContent = " + responseContent);
    }

}
