package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskHandler extends CommonHandler {
    private ArrayList<Task> tasks = new ArrayList<Task>();
    private ObjectMapper objectMapper = new ObjectMapper();

    private String taskToJson(Task task) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(task);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("e: " + e);
        }
        System.out.println("JSON string: " + jsonString);
        return jsonString;
    }

    private Task jsonToTask(String json) {
        Task task = null;
        try {
            task = objectMapper.readValue(json, Task.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("e: " + e);
        }
        System.out.println("Task: " + task);
        return task;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        System.out.println(method);
        if(Objects.equals(method, "GET")){
            handleGetMethod(exchange);
        }else if(Objects.equals(method, "POST")){
            handlePostMethod(exchange);
        }else if(Objects.equals(method, "PUT") || Objects.equals(method, "PATCH")){
            handlePutOrPatchMethod(exchange);
        }else if(Objects.equals(method, "DELETE")){
            handleDeleteMethod(exchange);
        }
    }

    private void handleGetMethod(HttpExchange exchange) throws IOException {
        String content = "Task: GET";
        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputResponse(exchange, content);
    }

    private void handlePostMethod(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")).lines().collect(Collectors.joining());
        Task task = jsonToTask(requestBody);
        task.setId(tasks.size() + 1);
        tasks.add(task);
        String content = taskToJson(task);
        exchange.sendResponseHeaders(201, content.getBytes().length);
        outputResponse(exchange, content);
    }

    private void handlePutOrPatchMethod(HttpExchange exchange) throws IOException {
        String content = "Task: PUT or PATCH";
        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputResponse(exchange, content);
    }

    private void handleDeleteMethod(HttpExchange exchange) throws IOException {
        String content = "Task: DELETE";
        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputResponse(exchange, content);
    }
}
