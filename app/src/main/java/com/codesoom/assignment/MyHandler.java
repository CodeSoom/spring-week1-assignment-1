package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MyHandler implements HttpHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Long, Task> taskMap = new ConcurrentHashMap<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());
        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();
        final OutputStream outputStream = exchange.getResponseBody();

        if (!Validator.isValid(path, httpMethod)) {
            exchange.sendResponseHeaders(HttpStatusCode.BAD_REQUEST.code, 0);
            writeAndFlushAndClose("", outputStream);
            return;
        }

        final String[] pathArr = path.split("/");

        String content = null;

        if (pathArr.length == 3) {
            final Long id = Long.valueOf(pathArr[2]);

            if (HttpMethod.GET.equals(httpMethod)) {
                content = findTaskById(id, exchange);
            }

            if (HttpMethod.PUT.equals(httpMethod) || HttpMethod.PATCH.equals(httpMethod)) {
                content = editTaskById(id, exchange);
            }

            if (HttpMethod.DELETE.equals(httpMethod)) {
                deleteTaskById(id, exchange);
                content = "";
            }

            writeAndFlushAndClose(content, outputStream);
            return;
        }

        if (HttpMethod.GET.equals(httpMethod)) {
            content = findAllTasks(exchange);
        }

        if (HttpMethod.POST.equals(httpMethod)) {
            content = addTask(exchange);
        }

        writeAndFlushAndClose(content, outputStream);
    }

    private void writeAndFlushAndClose(String content, OutputStream outputStream) throws IOException {
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String editTaskById(Long id, HttpExchange exchange) throws IOException {
        final Task originalTask = taskMap.get(id);
        String content;

        if (originalTask == null) {
            content = "";
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.code, content.getBytes().length);
            return content;
        }

        final InputStream inputStream = exchange.getRequestBody();
        final String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        final Task newTask = toTask(body);
        originalTask.setTitle(newTask.getTitle());

        content = taskToJson(originalTask);
        exchange.sendResponseHeaders(HttpStatusCode.OK.code, content.getBytes().length);

        return content;
    }

    private String addTask(HttpExchange exchange) throws IOException {
        final InputStream inputStream = exchange.getRequestBody();
        final String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        final Task task = toTask(body);
        task.setId();
        taskMap.put(task.getId(), task);

        final String content = taskToJson(task);
        exchange.sendResponseHeaders(HttpStatusCode.CREATED.code, content.getBytes().length);
        return content;
    }

    private String findAllTasks(HttpExchange exchange) throws IOException {
        final String content = tasksToJson();
        exchange.sendResponseHeaders(HttpStatusCode.OK.code, content.getBytes().length);
        return content;
    }

    private void deleteTaskById(Long id, HttpExchange exchange) throws IOException {
        final Task removedTask = taskMap.remove(id);

        if (removedTask == null) {
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.code, 0);
            return;
        }

        exchange.sendResponseHeaders(HttpStatusCode.NO_CONTENT.code, -1);
    }

    private String findTaskById(Long id, HttpExchange exchange) throws IOException {
        final Task task = taskMap.get(id);
        String content;

        if (task == null) {
            content = "";
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.code, content.getBytes().length);
            return content;
        }

        content = taskToJson(task);
        exchange.sendResponseHeaders(HttpStatusCode.OK.code, content.getBytes().length);

        return content;
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskMap.values());

        return outputStream.toString();
    }

    private String taskToJson(Task task) throws JsonProcessingException {
        return objectMapper.writeValueAsString(task);
    }
}
