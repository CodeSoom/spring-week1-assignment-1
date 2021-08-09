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
    private long id = 0L;

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

        System.out.println(method + " " + path);


        String content = "Hello, world!";

        System.out.println("test");
        if(method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJSON();
            System.out.println("/tasks");
            httpExchange.sendResponseHeaders(200, content.getBytes().length);
        }


        else if(method.equals("GET") && path.equals("/tasks/{id}")) {
            System.out.println("/tasks/{id}");
            content = tasksToJSON();
            httpExchange.sendResponseHeaders(200, content.getBytes().length);
        }

        else if(method.equals("POST") && path.equals("/tasks")) {
            content = "Create a new task.";
            if (!body.isEmpty()) {
                Task task = toTask(body);
                ++id;
                task.setId(id);

                tasks.add(task);

            }
            httpExchange.sendResponseHeaders(201, content.getBytes().length);
        }

        else if(method.equals("Patch") && path.equals("/tasks")) {
//            Task task = tasks.get()
            httpExchange.sendResponseHeaders(200, content.getBytes().length);
        }

        else if(method.equals("Delete") && path.equals("/tasks")) {
//            tasks.remove()
            content = "Delete task.";
            httpExchange.sendResponseHeaders(200, content.getBytes().length);
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
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
