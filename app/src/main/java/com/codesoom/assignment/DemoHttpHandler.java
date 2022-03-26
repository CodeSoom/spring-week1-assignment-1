package com.codesoom.assignment;

import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.domain.TaskList;
import com.codesoom.assignment.dto.TaskDto;
import com.codesoom.assignment.http.HttpMethod;
import com.codesoom.assignment.http.HttpStatusCode;
import com.codesoom.assignment.utils.TaskUriParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.codesoom.assignment.http.HttpMethod.*;
import static com.codesoom.assignment.http.HttpStatusCode.*;

public class DemoHttpHandler implements HttpHandler {

    private static final String LINE_BREAK = "\n";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final TaskList taskList;

    public DemoHttpHandler() {
        taskList = new TaskList();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestPathUri = exchange.getRequestURI().getPath();

        TaskUriParser uriParser = new TaskUriParser(requestPathUri);

        if (uriParser.isInvalidPath()) {
            response(exchange, NOT_FOUND);
            return;
        }

        HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());

        if (uriParser.hasId() && httpMethod.equals(GET)) {
            getTask(exchange, uriParser.getId());
        } else if (uriParser.hasId() && httpMethod.equals(PUT)) {
            changeTask(exchange, uriParser.getId());
        } else if (uriParser.hasId() && httpMethod.equals(PATCH)) {
            modifyTask(exchange, uriParser.getId());
        } else if (uriParser.hasId() && httpMethod.equals(DELETE)) {
            deleteTask(exchange, uriParser.getId());
        } else if (uriParser.hasNotId() && httpMethod.equals(POST)) {
            saveTask(exchange);
        } else if (uriParser.hasNotId() && httpMethod.equals(GET)) {
            getTasks(exchange);
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

        Task task = taskDto.toTask();
        Task savedTask = taskList.save(task);

        String content = objectMapper.writeValueAsString(savedTask);
        response(exchange, CREATED, content);
    }

    private void getTasks(final HttpExchange exchange) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, taskList.getTasks());

        String content = outputStream.toString();
        response(exchange, OK, content);
    }

    private void getTask(final HttpExchange exchange, final Long taskId) throws IOException {

        Optional<Task> findTask = taskList.findTaskById(taskId);
        if (findTask.isEmpty()) {
            response(exchange, NOT_FOUND);
            return;
        }
        Task task = findTask.get();

        String content = objectMapper.writeValueAsString(task);
        response(exchange, OK, content);
    }

    private void changeTask(final HttpExchange exchange, final Long taskId) throws IOException {

        String requestBody = getRequestBody(exchange);
        if (requestBody.isBlank()) {
            response(exchange, BAD_REQUEST);
            return;
        }

        Optional<Task> findTask = taskList.findTaskById(taskId);
        if (findTask.isEmpty()) {
            response(exchange, NOT_FOUND);
            return;
        }
        Task task = findTask.get();

        TaskDto taskDto = objectMapper.readValue(requestBody, TaskDto.class);
        task.setTitle(taskDto.getTitle());

        String content = objectMapper.writeValueAsString(task);
        response(exchange, OK, content);
    }

    private void modifyTask(final HttpExchange exchange, final Long taskId) throws IOException {

        Optional<Task> findTask = taskList.findTaskById(taskId);
        if (findTask.isEmpty()) {
            response(exchange, NOT_FOUND);
            return;
        }
        Task task = findTask.get();

        String requestBody = getRequestBody(exchange);
        if (requestBody.isBlank()) {
            response(exchange, BAD_REQUEST);
            return;
        }

        TaskDto taskDto = objectMapper.readValue(requestBody, TaskDto.class);
        if (taskDto.getTitle() != null) {
            task.setTitle(taskDto.getTitle());
        }

        String content = objectMapper.writeValueAsString(task);
        response(exchange, OK, content);
    }

    private void deleteTask(final HttpExchange exchange, final Long taskId) throws IOException {

        Optional<Task> findTask = taskList.findTaskById(taskId);
        if (findTask.isEmpty()) {
            response(exchange, NOT_FOUND);
            return;
        }
        Task task = findTask.get();

        taskList.remove(task);
        response(exchange, NO_CONTENT);
    }

    private void response(final HttpExchange exchange, final HttpStatusCode statusCode) throws IOException {

        long responseLength = statusCode.equals(NO_CONTENT) ? -1 : 0;

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
                .collect(Collectors.joining(LINE_BREAK));
    }
}
