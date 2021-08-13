package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int NOT_FOUND = 404;
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final String TASKS_PATH = "/tasks";
    private static final String TASK_DETAIL_PATH = "/tasks/";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Task> tasks = new ArrayList<>();
    private Long id = 0L;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String body = body(exchange.getRequestBody());

        if (requestMethod.equals(GET_METHOD) && path.equals(TASKS_PATH)) {
            sendResponseBody(exchange, new ResponseData(OK, tasksToJson()));
            return;
        }

        if (requestMethod.equals(GET_METHOD) && path.startsWith(TASK_DETAIL_PATH)) {
            sendResponseBody(exchange, taskDetailResponseData(findTask(taskId(path))));
            return;
        }

        if (requestMethod.equals(POST_METHOD) && path.equals(TASKS_PATH)) {
            Task addTask = bodyToTask(body);
            addTask.setId(increaseId());
            this.tasks.add(addTask);
            sendResponseBody(exchange, new ResponseData(CREATED, taskToJson(addTask)));
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

    private ResponseData taskDetailResponseData(Task task) throws IOException {
        if (!Objects.isNull(task)) {
            return new ResponseData(OK, taskToJson(task));
        }
        return new ResponseData(NOT_FOUND, "");
    }

    private Long taskId(String path) {
        return Long.parseLong(path.split("/")[2]);
    }

    private Task findTask(Long taskId) {
        return tasks.stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElse(null);
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
