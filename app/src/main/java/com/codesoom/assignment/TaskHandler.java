package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHandler {

    ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();


    public void handler(String method, String path, String body, HttpExchange exchange) throws IOException {
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        System.out.println(method + " " + path + "[ " + body + " ]");

        // Method
        switch (method){
            case "GET" :
                System.out.println("GET");
                break;
            case "POST" :
                System.out.println("POST");
                break;
            case "PUT", "PATCH" :
                System.out.println("PUT, PATCH");
                break;
            default:
                break;
        }

        String content = tasks.toString();

        content = tasksToJson();

        Task task = toTask(body);
        System.out.println(task);



        exchange.sendResponseHeaders(200, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
