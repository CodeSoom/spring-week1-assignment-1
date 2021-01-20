package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpStatus;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private int index;
    private int statusCode = HttpStatus.OK;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/") || path.startsWith("/tasks")) {
            sendResponse(exchange, processRequest(exchange));
            return;
        }
        statusCode=HttpStatus.NOT_FOUND;
        sendResponse(exchange,"");
    }

    private String processRequest(HttpExchange exchange) throws IOException {
        String content = "";
        switch (exchange.getRequestMethod()) {
            case "GET":
                content = processGetRequest(exchange);
                break;
            case "POST":
                content = processPostRequest(exchange);
                break;
            case "PUT":
            case "PATCH":
                content = processPutAndPatchRequest(exchange);
                break;
            case "DELETE":
                processDeleteRequest(exchange);
                break;
            case "HEAD":
                statusCode=HttpStatus.OK;
                break;
        }
        return content;
    }

    private void processDeleteRequest(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        if (!hasNumberParameter(path) || !hasIndex(index - 1)) {
            statusCode = HttpStatus.NOT_FOUND;
            return;
        }
        statusCode = HttpStatus.NO_CONTENT;
        tasks.remove(index - 1);
    }

    private String processPutAndPatchRequest(HttpExchange exchange) throws IOException {
        String body = getStringBody(exchange);
        String path = exchange.getRequestURI().getPath();
        String content = "";
        if (!hasNumberParameter(path) || !hasIndex(index - 1)) {
            statusCode = HttpStatus.NOT_FOUND;
            return content;
        }
        tasks.remove(index - 1);
        Task task = jsonToTask(body);
        task.setId((long) index);
        tasks.add(index - 1, task);
        statusCode = HttpStatus.OK;
        content = taskToJson(index - 1);

        return content;
    }

    private String processPostRequest(HttpExchange exchange) throws IOException {
        String body = getStringBody(exchange);
        Task task = jsonToTask(body);
        tasks.add(task);
        statusCode = HttpStatus.CREATED;
        String content = taskToJson(task.getId() - 1);
        return content;
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
            String content = taskToJson();
            return content;
        }

        if (!hasIndex(index - 1)) {
            statusCode = HttpStatus.NOT_FOUND;
            String content = "";
            return content;
        }
        statusCode = HttpStatus.OK;
        String content = taskToJson(index - 1);

        return content;
    }

    private void sendResponse(HttpExchange exchange, String content) throws IOException {
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(content.getBytes());
            outputStream.flush();
        }
    }

    private boolean hasIndex(int index) {
        if (index < 0 || tasks.size() <= index) {
            return false;
        }
        return true;
    }

    private boolean hasNumberParameter(String path) {
        String[] split = path.split("/tasks/");
        if (split.length == 2) {
            index = Integer.parseInt(split[1]);
            return true;
        }

        return false;
    }

    private Task jsonToTask(String content) throws JsonProcessingException {
        Task task = mapper.readValue(content, Task.class);
        task.setId((long) (tasks.size() + 1));
        return task;
    }

    private String taskToJson() throws IOException {
        OutputStream outputstream = new ByteArrayOutputStream();
        mapper.writeValue(outputstream, tasks);
        return outputstream.toString();
    }

    private String taskToJson(long index) throws IOException {
        OutputStream outputstream = new ByteArrayOutputStream();
        mapper.writeValue(outputstream, tasks.get((int) index));
        return outputstream.toString();
    }
}
