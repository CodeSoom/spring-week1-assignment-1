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

public class TaskHttpHandler implements HttpHandler {
    private List<Task> taskList = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String content = "[]";
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));


        if(!body.isBlank()){
            Task task = makeTask(body);
        }

        exchange.sendResponseHeaders(200, content.getBytes().length );
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private Task makeTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }
}
