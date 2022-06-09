package com.codesoom.todo.controllers;

import com.codesoom.todo.models.Task;
import com.codesoom.todo.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ToDoController {
    private final TaskService taskService;

    public ToDoController(TaskService taskService) {
        this.taskService = taskService;
    }

    public void showTask(HttpExchange exchange, Long id) throws IOException {
        Task currentTask = this.taskService.getTask(id);
        if (currentTask == null){
            sendResponse(exchange, 404, "Task not found");
        } else {
            sendResponse(exchange, 200, taskToString(currentTask));
        }
    }


    public void showTasks(HttpExchange exchange) throws IOException {
        ConcurrentHashMap<Long, Task> currentTasks= this.taskService.getTasks();
        if (currentTasks == null){
            sendResponse(exchange, 404, "Task not found");
        } else {
            sendResponse(exchange, 200, tasksToString(currentTasks));
        }
    }

    public void createTask(HttpExchange exchange) throws IOException {
        String requestBody = getRequestBody(exchange);
        Task newTask = stringToTask(requestBody);
        Long createdID = taskService.addTask(newTask);

        sendResponse(exchange, 201, taskToString(taskService.getTask(createdID)));
    }


    public void editTaskTitleByID(HttpExchange exchange, Long id) {

    }

    public void deleteTaskByID(HttpExchange exchange, Long id) {

    }

    public void errorResponse() {

    }
    private String taskToJson(Task newTask)  {
        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            objectMapper.writeValue(outputStream, newTask);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream.toString();
    }

    private String taskToString(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String tasksToString(ConcurrentHashMap<Long, Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream, tasks.values());

        return outputStream.toString();
    }


    private Task stringToTask(String content) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(content, Task.class);
    }

    private String getRequestMethod(HttpExchange exchange){
        return exchange.getRequestMethod();
    }

    private String getRequestPath(HttpExchange exchange){
        return exchange.getRequestURI().getPath();
    }

    private String getRequestBody(HttpExchange exchange){
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }


    private void sendResponse(HttpExchange exchange, Integer statusCode, String responseContent) {
        try {
            exchange.sendResponseHeaders(statusCode, responseContent.getBytes().length);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseContent.getBytes());
            responseBody.flush();
            responseBody.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
