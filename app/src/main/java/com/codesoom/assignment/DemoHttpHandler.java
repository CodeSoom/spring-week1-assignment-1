package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class DemoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();

    public DemoHttpHandler() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Do nothing...");

        tasks.add(task);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        System.out.println(method+" "+path);

        String content = "Hello World";

        if(method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJson();
        }

        if(method.equals("POST") && path.equals("/task")) {
            content = "Create Just One Task";
        }

        exchange.sendResponseHeaders(200,content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String tasksToJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }
}
