package com.codesoom.assignment;

import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.domain.TaskList;
import com.codesoom.assignment.dto.TaskDto;
import com.codesoom.assignment.http.HttpMethod;
import com.codesoom.assignment.http.HttpStatusCode;
import com.codesoom.assignment.matcher.TaskUriMatcher;
import com.codesoom.assignment.matcher.UriMatcher;
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

    private static final long EMPTY_RESPONSE_LENGTH = 0;

    private static final long NO_CONTENT_RESPONSE_LENGTH = -1;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final UriMatcher uriMatcher = new TaskUriMatcher();

    private final TaskList taskList;

    public DemoHttpHandler() {
        taskList = new TaskList();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String pathUri = exchange.getRequestURI().getPath();

        String[] pathSegments = pathUri.split("/");

        if (!uriMatcher.isValidPath(pathSegments)) {
            response(exchange, NOT_FOUND);
            return;
        }

        HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());

        if (httpMethod.equals(POST) && !uriMatcher.hasId(pathSegments)) {
            saveTask(exchange);
        } else if (httpMethod.equals(GET) && !uriMatcher.hasId(pathSegments)) {
            getTasks(exchange);
        } else if (httpMethod.equals(GET) && uriMatcher.hasId(pathSegments)) {
            getTask(exchange, pathSegments);
        } else if (httpMethod.equals(PATCH) && uriMatcher.hasId(pathSegments)) {
            modifyTask(exchange, pathSegments);
        } else if (httpMethod.equals(PUT) && uriMatcher.hasId(pathSegments)) {
            changeTask(exchange, pathSegments);
        } else if (httpMethod.equals(DELETE) && uriMatcher.hasId(pathSegments)) {
            deleteTask(exchange, pathSegments);
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

    private void getTask(final HttpExchange exchange, final String[] pathSegments) throws IOException {

        Long taskId = uriMatcher.getId(pathSegments);

        Optional<Task> findTask = taskList.findTaskById(taskId);
        if (findTask.isEmpty()) {
            response(exchange, NOT_FOUND);
            return;
        }
        Task task = findTask.get();

        String content = objectMapper.writeValueAsString(task);
        response(exchange, OK, content);
    }

    private void changeTask(final HttpExchange exchange, final String[] pathSegments) throws IOException {

        String requestBody = getRequestBody(exchange);
        if (requestBody.isBlank()) {
            response(exchange, BAD_REQUEST);
            return;
        }

        Long taskId = uriMatcher.getId(pathSegments);
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

    private void modifyTask(final HttpExchange exchange, final String[] pathSegments) throws IOException {

        Long taskId = uriMatcher.getId(pathSegments);

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

    private void deleteTask(final HttpExchange exchange, final String[] pathSegments) throws IOException {

        Long taskId = uriMatcher.getId(pathSegments);

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
                .collect(Collectors.joining(LINE_BREAK));
    }
}
