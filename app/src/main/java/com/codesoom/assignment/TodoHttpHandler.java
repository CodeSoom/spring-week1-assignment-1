package com.codesoom.assignment;

import com.codesoom.assignment.models.RequestContent;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.models.TasksStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private TasksStorage tasks = new TasksStorage();
    private String content = "";
    private Integer statusCode = HttpStatus.InternalServerError.code();
    private Long id;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        boolean isTasksPath = "/tasks".equals(path);
        boolean isTasksPathWithId = path.length() > "/tasks/".length();

        if ("GET".equals(method) && isTasksPath) {
            handleGetRequest();
        }

        if ("GET".equals(method) && isTasksPathWithId) {
            id = Long.parseLong(path.substring(7));
            handleGetRequest(id);
        }

        if ("POST".equals(method) && isTasksPath && !body.isBlank()) {
            handlePostRequest(body);
        }

        if ("PUT".equals(method) && isTasksPathWithId && !body.isBlank()) {
            id = Long.parseLong(path.substring(7));
            handlePutRequest(id, body);
        }

        if ("DELETE".equals(method) && isTasksPathWithId) {
            id = Long.parseLong(path.substring(7));
            handleDeleteRequest(id);
        }

        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void handleGetRequest() throws IOException {
        content = tasksToJson(tasks.readAll());
        statusCode = HttpStatus.Ok.code();
    }

    private void handleGetRequest(Long id) throws IOException {
        Task task = tasks.read(id);

        if (task != null){
            content = taskToJson(task);
            statusCode = HttpStatus.Ok.code();
        } else {
            statusCode = HttpStatus.NotFound.code();
        }
    }

    private void handlePostRequest(String body) throws IOException {
        String title = toRequestContent(body).getTitle();

        Task task = tasks.create(title);

        statusCode = HttpStatus.Created.code();
        content = taskToJson(task);
    }

    private void handlePutRequest(Long id, String body) throws IOException {
        String title = toRequestContent(body).getTitle();

        Task task = tasks.update(id, title);

        if(task != null) {
            statusCode = HttpStatus.Ok.code();
            content = taskToJson(task);
        } else {
            statusCode = HttpStatus.NotFound.code();
        }
    }

    private void handleDeleteRequest(Long id) throws IOException {
        Task task = tasks.delete(id);

        if(task != null) {
            statusCode = HttpStatus.NoContent.code();
            content = taskToJson(task);
        } else {
            statusCode = HttpStatus.NotFound.code();
        }
    }

    private RequestContent toRequestContent(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, RequestContent.class);
    }

    private String tasksToJson(Collection<Task> tasks) throws IOException {
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
