package com.codesoom.assignment.handler;

import com.codesoom.assignment.dto.TaskDto;
import com.codesoom.assignment.models.MethodType;
import com.codesoom.assignment.models.StatusCode;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandler implements HttpHandler {
    private final static List<Task> tasks = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Pattern urlPattern = Pattern.compile("/tasks/(\\d)");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MethodType methodType = MethodType.valueOf(exchange.getRequestMethod());

        switch (methodType) {
            case GET:
                getTasksProcessor(exchange);
                break;
            case POST:
                postTasksProcessor(exchange);
                break;
            case PUT:
            case PATCH:
                putTasksProcessor(exchange);
                break;
            case DELETE:
                deleteTasksProcessor(exchange);
                break;
            default:
                throw new IllegalArgumentException("지원되지 않는 METHOD 타입입니다.");
        }
    }

    private void getTasksProcessor(HttpExchange exchange) throws IOException {
        String path = urlPath(exchange);
        Matcher matcher = urlPattern.matcher(path);

        if (path.equals("/tasks")) {
            String content = taskToJSON(tasks);
            returnOutputStream(exchange, content, StatusCode.OK);
        }

        if (matcher.find()) {
            Optional<Task> task = getTaskById(matcher);

            if (task.isEmpty()) {
                returnOutputStream(exchange, "", StatusCode.NotFound);
            } else {
                String content = taskToJSON(task.get());
                returnOutputStream(exchange, content, StatusCode.OK);
            }
        }
    }

    private void putTasksProcessor(HttpExchange exchange) throws IOException {
        String path = urlPath(exchange);
        Matcher matcher = urlPattern.matcher(path);

        if (matcher.find()) {
            Optional<Task> task = getTaskById(matcher);
            if (task.isEmpty()) {
                returnOutputStream(exchange, "", StatusCode.NotFound);
            } else {
                TaskDto taskDto = toTask(exchange);
                task.get().setTitle(taskDto.getTitle());
                String content = taskToJSON(task.get());
                returnOutputStream(exchange, content, StatusCode.OK);
            }
        }
    }

    private void postTasksProcessor(HttpExchange exchange) throws IOException {
        String path = urlPath(exchange);

        if (path.equals("/tasks")) {
            TaskDto taskDto = toTask(exchange);
            long currentId = tasks.stream().max(Comparator.comparingLong(Task::getId)).map(x -> x.getId()).orElse(0L);
            Task task = new Task(currentId + 1, taskDto.getTitle());
            tasks.add(task);

            String content = taskToJSON(task);
            returnOutputStream(exchange, content, StatusCode.CREATED);
        }
    }

    private void deleteTasksProcessor(HttpExchange exchange) throws IOException {
        String path = urlPath(exchange);
        Matcher matcher = urlPattern.matcher(path);

        if (matcher.find()) {
            Optional<Task> task = getTaskById(matcher);
            if (task.isEmpty()) {
                returnOutputStream(exchange, "", StatusCode.NotFound);
            } else {
                tasks.remove(task.get());
                returnOutputStream(exchange, "", StatusCode.NoContent);
            }
        }
    }

    private void returnOutputStream(HttpExchange exchange, String content, StatusCode statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode.getStatusCode(), content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String urlPath(HttpExchange exchange) {
        return exchange.getRequestURI().getPath();
    }

    private Optional<Task> getTaskById(Matcher matcher) {
        Optional<Task> task = tasks.stream().filter(x -> x.getId().equals(Long.parseLong(matcher.group(1))))
                .findFirst();
        return task;
    }

    private TaskDto toTask(HttpExchange exchange) throws IOException {
        return objectMapper.readValue(exchange.getRequestBody().readAllBytes(), TaskDto.class);
    }

    private <T> String taskToJSON(T task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }

}
