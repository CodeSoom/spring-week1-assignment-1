package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpStatus;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private Map<Long, Task> taskMap = new HashMap<>();
    private ObjectMapper mapper = new ObjectMapper();
    private long inputId;
    private long lastTaskId = 1L;
    private int statusCode = HttpStatus.OK;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/") || path.startsWith("/tasks")) {
            sendResponse(exchange, processRequest(exchange));
            return;
        }
        statusCode = HttpStatus.NOT_FOUND;
        sendResponse(exchange, "");
    }

    private String processRequest(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                return processGetRequest(exchange);
            case "POST":
                return processPostRequest(exchange);
            case "PUT":
            case "PATCH":
                return processPutAndPatchRequest(exchange);
            case "DELETE":
                return processDeleteRequest(exchange);
            case "HEAD":
                statusCode = HttpStatus.OK;
                return "";
            default:
                return "";
        }
    }

    private String processDeleteRequest(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        if (!hasNumberParameter(path)) {
            statusCode = HttpStatus.NOT_FOUND;
            return "";
        }
        inputId = extractNumber(path);
        if (!hasId(inputId)) {
            statusCode = HttpStatus.NOT_FOUND;
            return "";
        }
        statusCode = HttpStatus.NO_CONTENT;
        taskMap.remove(inputId);
        return "";
    }

    private long extractNumber(String path) {
        path = path.replace("/tasks/", "");
        if (path.matches("^[0-9]+$")) {
            return Long.parseLong(path);
        }
        return -1;
    }

    private String processPutAndPatchRequest(HttpExchange exchange) throws IOException {
        String body = getStringBody(exchange);
        String path = exchange.getRequestURI().getPath();
        if (!hasNumberParameter(path)) {
            statusCode = HttpStatus.NOT_FOUND;
            return "";
        }
        inputId = extractNumber(path);
        if (!hasId(inputId)) {
            statusCode = HttpStatus.NOT_FOUND;
            return "";
        }
        Task task = jsonToTask(body, inputId);
        taskMap.put(inputId, task);
        statusCode = HttpStatus.OK;
        return taskToJson(inputId);
    }

    private String processPostRequest(HttpExchange exchange) throws IOException {
        String body = getStringBody(exchange);
        Task task = jsonToTask(body);
        taskMap.put(lastTaskId, task);
        statusCode = HttpStatus.CREATED;
        return taskToJson(lastTaskId++);
    }

    private String getStringBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private String processGetRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (!hasNumberParameter(path)) {
            statusCode = HttpStatus.OK;
            return taskToJson();
        }
        inputId = extractNumber(path);
        if (!hasId(inputId)) {
            statusCode = HttpStatus.NOT_FOUND;
            return "";
        }
        statusCode = HttpStatus.OK;
        return taskToJson(inputId);
    }

    private void sendResponse(HttpExchange exchange, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(content.getBytes());
            outputStream.flush();
        }
    }

    private boolean hasId(long id) {
        if (taskMap.containsKey(id)) {
            return true;
        }
        return false;
    }

    private boolean hasNumberParameter(String path) {
        path = path.replace("/tasks/", "");
        if (path.matches("^[0-9]+$")) {
            return true;
        }
        return false;
    }

    private Task jsonToTask(String content) throws JsonProcessingException {
        Task task = mapper.readValue(content, Task.class);
        task.setId(lastTaskId);
        return task;
    }

    private Task jsonToTask(String content, long inputId) throws JsonProcessingException {
        Task task = mapper.readValue(content, Task.class);
        task.setId(inputId);
        return task;
    }

    private String taskToJson() throws IOException {
        OutputStream outputstream = new ByteArrayOutputStream();
        mapper.writeValue(outputstream, new ArrayList<>(taskMap.values()));
        return outputstream.toString();
    }

    private String taskToJson(long id) throws IOException {
        OutputStream outputstream = new ByteArrayOutputStream();
        mapper.writeValue(outputstream, taskMap.get(id));
        return outputstream.toString();
    }
}
