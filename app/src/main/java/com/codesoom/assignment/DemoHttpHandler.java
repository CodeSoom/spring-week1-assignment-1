package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    private HashMap<Long, Task> tasks = new HashMap<>();
    private long id = 0L;
    private int httpStatusCode = 200;
    private int length = 0;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ....
        // 2. Path - "/tasks", "tasks/1", ...
        // 3. Headers, Body(Content)

        String method = httpExchange.getRequestMethod();

        URI uri = httpExchange.getRequestURI();

        String path = uri.getPath();

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path + " " + body);
        String[] pathElements = path.split("/");


        String content = "Hello, world!";

        if(method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
            System.out.println("/tasks");
            httpExchange.sendResponseHeaders(200, content.getBytes().length);
        }


        else if("GET".equals(method) && pathElements.length > 2) {

            id = Long.parseLong(pathElements[pathElements.length - 1]);
            if(tasks.get(id) != null) {
                content = taskToJSON(id);
                httpStatusCode = 200;
                length = content.getBytes().length;
            } else {
                httpStatusCode = 404;
                length = 0;
            }

            httpExchange.sendResponseHeaders(httpStatusCode, length);
        }

        else if("POST".equals(method) && path.equals("/tasks")) {
            if (!body.isEmpty()) {
                Task task = toTask(body);
                ++id;
                task.setId(id);
                tasks.put(id, task);
                content = taskToJSON(id);
            }
            httpExchange.sendResponseHeaders(201, content.getBytes().length);
        }

        else if(("PATCH".equals(method) || method.equals("PUT")) && pathElements.length > 2) {
            id = Long.parseLong(pathElements[pathElements.length - 1]);
            if (!body.isEmpty() && tasks.get(id) != null) {
                Task task = toTask(body);
                task.setId(id);
                tasks.put(id, task);
                httpStatusCode = 200;
            } else {
                httpStatusCode = 404;
            }
            content = taskToJSON(id);
            httpExchange.sendResponseHeaders(httpStatusCode, content.getBytes().length);
        }

        else if("DELETE".equals(method) && pathElements.length > 2) {
            id = Long.parseLong(pathElements[pathElements.length - 1]);
            if(tasks.get(id) != null) {
                tasks.remove(id);
                content = "Delete task.";
                httpStatusCode = 204;
            } else {
                content = "존재하지 않음";
                httpStatusCode = 404;
            }

            httpExchange.sendResponseHeaders(httpStatusCode, content.getBytes().length);
        }



        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, new ArrayList(tasks.values()));

        return outputStream.toString();
    }

    private String taskToJSON(Long id) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks.get(id));

        return outputStream.toString();
    }
}
