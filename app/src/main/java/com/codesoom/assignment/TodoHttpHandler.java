package com.codesoom.assignment;

import com.codesoom.assignment.models.Path;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.response.*;
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
    private final Map<Long, Task> taskMap = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private long lastTaskId = 1L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Path path = new Path(exchange.getRequestURI().getPath());
        if (path.checkUrl()) {
            Response response = processRequest(exchange);
            response.sendResponse(exchange);
            return;
        }
        Response response = new ResponseNoContent("");
        response.sendResponse(exchange);
    }

    private Response processRequest(HttpExchange exchange) throws IOException {
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
                return new ResponseSuccess("");
            default:
                return new ResponseNotFound("");
        }
    }

    private Response processDeleteRequest(HttpExchange exchange) {
        Path path = new Path(exchange.getRequestURI().getPath());
        if (!path.hasNumberParameter()) {
            return new ResponseNotFound("");
        }
        long inputId = path.extractNumber();
        if (!hasId(inputId)) {
            return new ResponseNotFound("");
        }
        taskMap.remove(inputId);
        return new ResponseNoContent("");
    }

    private Response processPutAndPatchRequest(HttpExchange exchange) throws IOException {
        String body = getStringBody(exchange);
        Path path = new Path(exchange.getRequestURI().getPath());
        if (!path.hasNumberParameter()) {
            return new ResponseNotFound("");
        }
        long inputId = path.extractNumber();
        if (!hasId(inputId)) {
            return new ResponseNotFound("");
        }
        Task task = jsonToTask(body);
        taskMap.put(inputId, task);
        return new ResponseSuccess(taskToJson(inputId));
    }

    private Response processPostRequest(HttpExchange exchange) throws IOException {
        String body = getStringBody(exchange);
        Task task = jsonToTask(body);
        taskMap.put(lastTaskId, task);
        return new ResponseCreated(taskToJson(lastTaskId++));
    }

    private String getStringBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Response processGetRequest(HttpExchange exchange) throws IOException {
        Path path = new Path(exchange.getRequestURI().getPath());
        if (!path.hasNumberParameter()) {
            return new ResponseSuccess(taskToJson());
        }
        long inputId = path.extractNumber();
        if (!hasId(inputId)) {
            return new ResponseNotFound("");
        }
        return new ResponseSuccess(taskToJson(inputId));
    }


    private boolean hasId(long id) {
        return taskMap.containsKey(id);
    }

    private Task jsonToTask(String content) throws JsonProcessingException {
        Task task = mapper.readValue(content, Task.class);
        task.setId(lastTaskId);
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
