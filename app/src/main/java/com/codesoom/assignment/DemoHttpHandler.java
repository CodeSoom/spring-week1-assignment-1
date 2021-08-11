package com.codesoom.assignment;

import com.codesoom.assignment.models.RequestInfo;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private Long nextId = 1L;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // get method, url, body
        RequestInfo requestInfo = new RequestInfo(exchange);

        System.out.println(requestInfo.getMethod() + requestInfo.getPath());

        String content = "";

        // GET ALL
        if ("GET".equals(requestInfo.getMethod()) && hasTask(requestInfo.getPath())) {
            content = convertTasksToJson(this.tasks);
        }

        // GET Detail
        if ("GET".equals(requestInfo.getMethod()) && hasTaskId(requestInfo.getPath())) {
            Long id = getTaskIdFromPath(requestInfo.getPath());
            Task resultTask = getTaskById(id);

            content = convertTaskToJson(resultTask);
        }

        // POST
        if ("POST".equals(requestInfo.getMethod()) && hasTask(requestInfo.getPath())) {
            Task task = convertJsonToTask(requestInfo.getBody());
            task.setId(this.nextId++);
            this.tasks.add(task);

            content = convertTaskToJson(task);
        }

        // PUT
        if ("PUT".equals(requestInfo.getMethod()) && hasTaskId(requestInfo.getPath())) {
            Long id = getTaskIdFromPath(requestInfo.getPath());
            String inputTitle = convertJsonToTask(requestInfo.getBody()).getTitle();

            Task resultTask = getTaskById(id);
            resultTask.setTitle(inputTitle);

            content = convertTaskToJson(resultTask);
        }

        // DELETE
        if ("DELETE".equals(requestInfo.getMethod()) && hasTaskId(requestInfo.getPath())) {
            Long id = getTaskIdFromPath(requestInfo.getPath());
            removeTaskById(id);
        }

        exchange.sendResponseHeaders(200, content.getBytes().length);

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

    private Task getTaskById(Long id) {
        return this.tasks.stream()
                .filter(task->task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void removeTaskById(Long id) {
        this.tasks = this.tasks.stream()
                .filter(task->!(task.getId().equals(id)))
                .collect(Collectors.toList());
    }

    private boolean hasTask(String path) {
        return Pattern.matches("/tasks$", path);
    }

    private boolean hasTaskId(String path) {
        return Pattern.matches("/tasks/[0-9]+$", path);
    }

    private Long getTaskIdFromPath(String path) {
        Pattern pattern = Pattern.compile("/tasks/([0-9]+)$");
        Matcher matcher = pattern.matcher(path);

        if(!matcher.find())
            return null;

        return Long.parseLong(matcher.group(1));
    }
}
