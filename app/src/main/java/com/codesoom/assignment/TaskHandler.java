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
    public static ArrayList<Task> tasks = new ArrayList<Task>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        System.out.println(method);
        if(Objects.equals(method, "GET")){
            handleGetMethod(exchange);
        }else if(Objects.equals(method, "POST")){
            handlePostMethod(exchange);
        }
    }

    private void handleGetMethod(HttpExchange exchange) throws IOException {
        TaskSerializer taskSerializer = new TaskSerializer();
        String content = taskSerializer.tasksToJson(tasks);
        exchange.sendResponseHeaders(200, content.getBytes().length);
        outputResponse(exchange, content);
    }

    private void handlePostMethod(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")).lines().collect(Collectors.joining());
        TaskSerializer taskSerializer = new TaskSerializer();
        Task task = taskSerializer.jsonToTask(requestBody);
        task.setId(tasks.size() + 1);
        tasks.add(task);
        String content = taskSerializer.taskToJson(task);
        exchange.sendResponseHeaders(201, content.getBytes().length);
        outputResponse(exchange, content);
    }
}
