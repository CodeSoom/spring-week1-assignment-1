package com.codesoom.assignment;

import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.domain.TaskList;
import com.codesoom.assignment.dto.TaskDto;
import com.codesoom.assignment.http.HttpMethod;
import com.codesoom.assignment.http.HttpStatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.codesoom.assignment.http.HttpMethod.*;
import static com.codesoom.assignment.http.HttpStatusCode.*;

public class DemoHttpHandler implements HttpHandler {

    private static final String PATH_SEPARATOR = "/";

    private static final long EMPTY_RESPONSE_LENGTH = 0;

    private static final long NO_CONTENT_RESPONSE_LENGTH = -1;

    private static final int RESOURCE_ID_POSITION = 2;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final TaskList taskList;

    public DemoHttpHandler() {
        taskList = new TaskList();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String pathURI = exchange.getRequestURI().getPath();

        if (!pathURI.startsWith("/tasks")) {
            response(exchange, NOT_FOUND);
            return;
        }

        String[] pathArr = pathURI.split(PATH_SEPARATOR);
        HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());

        if (httpMethod.equals(POST) && pathArr.length == 2) {
            saveTask(exchange);
        } else if (httpMethod.equals(GET) && pathArr.length == 2) {
            getTasks(exchange);
        } else if (httpMethod.equals(GET) && pathArr.length == 3) {
            getTask(exchange, pathArr);
        } else if (httpMethod.equals(PATCH) && pathArr.length == 3) {
            modifyTask(exchange, pathArr);
        } else if (httpMethod.equals(PUT) && pathArr.length == 3) {
            changeTask(exchange, pathArr);
        } else if (httpMethod.equals(DELETE) && pathArr.length == 3) {
            deleteTask(exchange, pathArr);
        } else {
            response(exchange, BAD_REQUEST);
        }
    }

    private void saveTask(final HttpExchange exchange) throws IOException {

        String requestBody = getRequestBody(exchange);
        if (requestBody.isBlank()) {
            response(exchange, BAD_REQUEST);
            return;
        }

        TaskDto taskDto = objectMapper.readValue(requestBody, TaskDto.class);

        Task savedTask = taskList.save(taskDto);

        String content = objectMapper.writeValueAsString(savedTask);
        response(exchange, CREATED, content);
    }

    private void getTasks(final HttpExchange exchange) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, taskList.getTasks());

        String content = outputStream.toString();
        response(exchange, OK, content);
    }

    private void getTask(final HttpExchange exchange, final String[] pathArr) throws IOException {

        Long taskId = getTaskId(pathArr);

        Optional<Task> taskOptional = taskList.findTaskById(taskId);
        if (taskOptional.isEmpty()) {
            response(exchange, NOT_FOUND);
            return;
        }
        Task findTask = taskOptional.get();

        String content = objectMapper.writeValueAsString(findTask);
        response(exchange, OK, content);
    }

    private void changeTask(final HttpExchange exchange, final String[] pathArr) throws IOException {

        String requestBody = getRequestBody(exchange);
        if (requestBody.isBlank()) {
            response(exchange, BAD_REQUEST);
            return;
        }

        Long taskId = getTaskId(pathArr);
        Optional<Task> taskOptional = taskList.findTaskById(taskId);
        if (taskOptional.isEmpty()) {
            response(exchange, NOT_FOUND);
            return;
        }
        Task findTask = taskOptional.get();

        TaskDto taskDto = objectMapper.readValue(requestBody, TaskDto.class);
        findTask.setTitle(taskDto.getTitle());

        String content = objectMapper.writeValueAsString(findTask);
        response(exchange, OK, content);
    }

    private void modifyTask(final HttpExchange exchange, final String[] pathArr) throws IOException {

        Long taskId = getTaskId(pathArr);

        Optional<Task> taskOptional = taskList.findTaskById(taskId);
        if (taskOptional.isEmpty()) {
            response(exchange, NOT_FOUND);
            return;
        }
        Task findTask = taskOptional.get();

        String requestBody = getRequestBody(exchange);
        if (requestBody.isBlank()) {
            response(exchange, BAD_REQUEST);
            return;
        }

        TaskDto taskDto = objectMapper.readValue(requestBody, TaskDto.class);
        if (taskDto.getTitle() != null) {
            findTask.setTitle(taskDto.getTitle());
        }

        String content = objectMapper.writeValueAsString(findTask);
        response(exchange, OK, content);
    }

    private void deleteTask(final HttpExchange exchange, final String[] pathArr) throws IOException {

        Long taskId = getTaskId(pathArr);

        Optional<Task> taskOptional = taskList.findTaskById(taskId);
        if (taskOptional.isEmpty()) {
            response(exchange, NOT_FOUND);
            return;
        }
        Task findTask = taskOptional.get();

        taskList.remove(findTask);
        response(exchange, NO_CONTENT);
    }

    private void response(final HttpExchange exchange, final HttpStatusCode statusCode) throws IOException {

        long responseLength = statusCode.equals(NO_CONTENT) ? NO_CONTENT_RESPONSE_LENGTH : EMPTY_RESPONSE_LENGTH;

        exchange.sendResponseHeaders(statusCode.getCode(), responseLength);

        OutputStream responseOutputStream = exchange.getResponseBody();
        responseOutputStream.close();
    }

    private void response(final HttpExchange exchange,
                          final HttpStatusCode statusCode,
                          final String content) throws IOException {

        exchange.sendResponseHeaders(statusCode.getCode(), content.getBytes().length);

        OutputStream responseOutputStream = exchange.getResponseBody();
        responseOutputStream.write(content.getBytes());
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    private String getRequestBody(final HttpExchange exchange) {

        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Long getTaskId(final String[] pathArr) throws NumberFormatException {
        return Long.valueOf(pathArr[RESOURCE_ID_POSITION]);
    }
}
