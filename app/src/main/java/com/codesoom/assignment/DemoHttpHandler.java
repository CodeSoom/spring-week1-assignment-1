package com.codesoom.assignment;

import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.dto.TaskDto;
import com.codesoom.assignment.http.HttpStatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.codesoom.assignment.http.HttpStatusCode.*;


public class DemoHttpHandler implements HttpHandler {

    private static final long EMPTY_RESPONSE_LENGTH = 0;

    private static final long NO_CONTENT_RESPONSE_LENGTH = -1;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final List<Task> taskList = new ArrayList<>();

    private long sequenceId = 0;

    public DemoHttpHandler() {
        Task task = new Task(++sequenceId, "title");
        taskList.add(task);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();

        if (!path.startsWith("/tasks")) {
            response(exchange, NOT_FOUND);
            return;
        }

        String[] pathArr = splitPath(path);

        String httpMethod = exchange.getRequestMethod();

        if (httpMethod.equals("POST") && pathArr.length == 2) {
            InputStream inputStream = exchange.getRequestBody();
            String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            TaskDto taskDto = objectMapper.readValue(requestBody, TaskDto.class);

            Task newTask = new Task(++sequenceId, taskDto.getTitle());
            taskList.add(newTask);

            String content = objectMapper.writeValueAsString(newTask);

            response(exchange, CREATED, content);
            return;
        }

        if (httpMethod.equals("GET") && pathArr.length == 2) {
            OutputStream outputStream = new ByteArrayOutputStream();
            objectMapper.writeValue(outputStream, taskList);
            String content = outputStream.toString();
            response(exchange, OK, content);
            return;
        }

        if (httpMethod.equals("GET") && pathArr.length == 3) {

            Optional<Task> taskOptional = findTaskById(pathArr);
            if (taskOptional.isEmpty()) {
                response(exchange, NOT_FOUND);
                return;
            }
            Task findTask = taskOptional.get();

            String content = objectMapper.writeValueAsString(findTask);
            response(exchange, OK, content);
            return;
        }

        if (httpMethod.equals("PATCH") && pathArr.length == 3) {
            Optional<Task> taskOptional = findTaskById(pathArr);
            if (taskOptional.isEmpty()) {
                response(exchange, NOT_FOUND);
                return;
            }
            Task findTask = taskOptional.get();

            InputStream inputStream = exchange.getRequestBody();
            String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

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
            return;
        }

        if (httpMethod.equals("PUT") && pathArr.length == 3) {
            Optional<Task> taskOptional = findTaskById(pathArr);
            if (taskOptional.isEmpty()) {
                response(exchange, NOT_FOUND);
                return;
            }
            Task findTask = taskOptional.get();

            InputStream inputStream = exchange.getRequestBody();
            String body = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            TaskDto taskDto = objectMapper.readValue(body, TaskDto.class);

            findTask.setTitle(taskDto.getTitle());

            String content = objectMapper.writeValueAsString(findTask);
            response(exchange, OK, content);
            return;
        }

        if (httpMethod.equals("DELETE") && pathArr.length == 3) {
            Optional<Task> taskOptional = findTaskById(pathArr);
            if (taskOptional.isEmpty()) {
                response(exchange, NOT_FOUND);
                return;
            }

            Task findTask = taskOptional.get();
            taskList.remove(findTask);
            response(exchange, NO_CONTENT);
            return;
        }
        response(exchange, BAD_REQUEST);
    }

    private String[] splitPath(String path) {
        return path.split("/");
    }

    // TODO - START - 분리 예정 -> task repository
    private Task findTaskById(Long taskId) {
        return taskList.stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElse(null);
    }
    // TODO - END

    private Optional<Task> findTaskById(String[] split) {

        if(split.length != 3) {
            return Optional.empty();
        }

        Long taskId = Long.valueOf(split[2]);

        Task findTask = findTaskById(taskId);

        if(findTask == null) {
            return Optional.empty();
        }

        return Optional.of(findTask);
    }

    private void response(final HttpExchange exchange,
                          final HttpStatusCode statusCode) throws IOException {

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
}
