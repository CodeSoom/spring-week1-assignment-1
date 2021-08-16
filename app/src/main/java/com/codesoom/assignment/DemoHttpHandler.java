package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int NOT_FOUND = 404;
    private static final int NO_CONTENT = 204;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String PATCH = "PATCH";
    private static final String DELETE = "DELETE";
    private static final String TASKS_PATH = "/tasks";
    private static final String TASK_DETAIL_PATH = "/tasks/";
    private static final String NO_CONTENTS = "";
    private static final String PATH_SPLIT_SYMBOL = "/";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Task> tasks = new ArrayList<>();
    private Long id = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String body = body(exchange.getRequestBody());

        if (requestMethod.equals(GET) && path.equals(TASKS_PATH)) {
            sendResponseBody(exchange, new ResponseData(OK, tasksToJson()));
            return;
        }

        if (requestMethod.equals(GET) && path.startsWith(TASK_DETAIL_PATH)) {
            try {
                Task task = findTask(taskId(path));
                sendResponseBody(exchange, new ResponseData(OK, taskToJson(task)));
            } catch (NoSuchElementException ne) {
                sendResponseBody(exchange, new ResponseData(NOT_FOUND, NO_CONTENTS));
            }
            return;
        }

        if (requestMethod.equals(POST) && path.equals(TASKS_PATH)) {
            Task task = bodyToTask(body);
            task.setId(increaseId());
            this.tasks.add(task);
            sendResponseBody(exchange, new ResponseData(CREATED, taskToJson(task)));
            return;
        }

        if ((requestMethod.equals(PUT) || requestMethod.equals(PATCH)) && path.startsWith(TASK_DETAIL_PATH)) {
            try {
                Task task = findTask(taskId(path));
                task.setTitle(body);
                sendResponseBody(exchange, new ResponseData(OK, body));
            } catch (NoSuchElementException ne) {
                sendResponseBody(exchange, new ResponseData(NOT_FOUND, NO_CONTENTS));
            }
            return;
        }

        if (requestMethod.equals(DELETE) && path.startsWith(TASK_DETAIL_PATH)) {
            if (tasks.removeIf(task -> task.getId().equals(taskId(path)))) {
                sendResponseBody(exchange, new ResponseData(NO_CONTENT, NO_CONTENTS));
                return ;
            }
            sendResponseBody(exchange, new ResponseData(NOT_FOUND, NO_CONTENTS));
        }
    }

    private String body(InputStream requestBody) {
        return new BufferedReader(new InputStreamReader(requestBody))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Task bodyToTask(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, Task.class);
    }

    private void sendResponseBody(HttpExchange exchange, ResponseData responseData) throws IOException {
        String contents = responseData.contents();
        exchange.sendResponseHeaders(responseData.statusCode(), contents.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(contents.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private Long taskId(String path) {
        return Long.parseLong(path.split(PATH_SPLIT_SYMBOL)[2]);
    }

    private Task findTask(Long taskId) {
        return tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private Long increaseId() {
        return this.id += 1L;
    }

    private String tasksToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }

    private String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }
}
