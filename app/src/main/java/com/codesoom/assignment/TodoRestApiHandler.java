package com.codesoom.assignment;

import com.codesoom.assignment.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TodoRestApiHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI url = exchange.getRequestURI();
        String path = url.getPath();

        String content = "Todo List";

        if(method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
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
}
