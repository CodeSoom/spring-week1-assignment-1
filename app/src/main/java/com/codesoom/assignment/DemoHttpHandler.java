package com.codesoom.assignment;

import com.codesoom.assignment.models.RequestInfo;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DemoHttpHandler implements HttpHandler {
    private TaskManager taskManger = new TaskManager();
    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // get method, url, body
        RequestInfo requestInfo = new RequestInfo(exchange);

        System.out.println(requestInfo.getMethod() + requestInfo.getPath());

        String requestMethod = requestInfo.getMethod();
        String requestPath = requestInfo.getPath();
        String requestBody = requestInfo.getBody();

        String content = "";
        int statusCode = 500;

        // GET ALL
        if ("GET".equals(requestMethod) && hasTask(requestPath)) {
            List<Task> allTasks = taskManger.getAllTasks();
            content = convertTasksToJson(allTasks);
            statusCode = 200;
        }

        // GET Detail
        if ("GET".equals(requestMethod) && hasTaskId(requestPath)) {
            try {
                Long id = getTaskIdFromPath(requestPath);
                Task resultTask = taskManger.getTaskById(id);
                content = convertTaskToJson(resultTask);
                statusCode = 200;
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                statusCode = 404;
            }
        }

        // POST
        if ("POST".equals(requestMethod) && hasTask(requestPath)) {
            Task task = convertJsonToTask(requestBody);
            Task newTask = taskManger.createTask(task.getTitle());
            content = convertTaskToJson(newTask);
            statusCode = 201;
        }

        // PUT & PATCH
        if (("PUT".equals(requestMethod) || "PATCH".equals(requestMethod)) && hasTaskId(requestPath)) {
            try {
                Long id = getTaskIdFromPath(requestPath);
                Task task = convertJsonToTask(requestBody);
                taskManger.updateTask(id, task.getTitle());
                content = convertTaskToJson(taskManger.getTaskById(id));
                statusCode = 200;
            } catch(NoSuchElementException e) {
                e.printStackTrace();
                statusCode = 404;
            }
        }

        // DELETE
        if ("DELETE".equals(requestMethod) && hasTaskId(requestPath)) {
            try {
                Long id = getTaskIdFromPath(requestPath);
                taskManger.removeTask(id);
                statusCode = 204;
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                statusCode = 404;
            }
        }

        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Task convertJsonToTask(String content) throws JsonProcessingException {
        return this.objectMapper.readValue(content, Task.class);
    }

    private String convertTasksToJson(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        this.objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String convertTaskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        this.objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private boolean hasTask(String path) {
        return Pattern.matches("/tasks/?$", path);
    }

    private boolean hasTaskId(String path) {
        return Pattern.matches("/tasks/[0-9]+/?$", path);
    }

    private Long getTaskIdFromPath(String path) throws NoSuchElementException {
        Pattern pattern = Pattern.compile("/tasks/([0-9]+)/?$");
        Matcher matcher = pattern.matcher(path);

        if(!matcher.find()) {
            throw new NoSuchElementException("Not Found id");
        }

        return Long.parseLong(matcher.group(1));
    }
}
