package com.codesoom.assignment;

import com.codesoom.assignment.models.RequestContent;
import com.codesoom.assignment.models.Response;
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
    private Response response;
    private Long id;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        response = new Response();

        boolean isTasksPath = "/tasks".equals(path);
        boolean isTasksPathWithId = path.length() > "/tasks/".length();

        if ("GET".equals(method) && isTasksPath) {
            response = handleGetRequest();
        }

        if ("GET".equals(method) && isTasksPathWithId) {
            id = Long.parseLong(path.substring(7));
            response = handleGetRequest(id);
        }

        if ("POST".equals(method) && isTasksPath && !body.isBlank()) {
            response = handlePostRequest(body);
        }

        if ("PUT".equals(method) && isTasksPathWithId && !body.isBlank()) {
            id = Long.parseLong(path.substring(7));
            response = handlePutRequest(id, body);
        }

        if ("DELETE".equals(method) && isTasksPathWithId) {
            id = Long.parseLong(path.substring(7));
            response = handleDeleteRequest(id);
        }

        exchange.sendResponseHeaders(response.getStatusCode(), response.getContentBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getContentBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Response handleGetRequest() throws IOException {
        String content = tasksToJson(tasks.readAll());
        HttpStatus httpStatus = HttpStatus.Ok;

        return new Response(content, httpStatus);
    }

    private Response handleGetRequest(Long id) throws IOException {
        Task task = tasks.read(id);

        if (task != null){
            String content = taskToJson(task);
            HttpStatus httpStatus = HttpStatus.Ok;

            return new Response(content, httpStatus);
        } else {
            return new Response(HttpStatus.NotFound);
        }
    }

    private Response handlePostRequest(String body) throws IOException {
        String title = toRequestContent(body).getTitle();

        Task task = tasks.create(title);

        String content = taskToJson(task);
        HttpStatus httpStatus = HttpStatus.Created;

        return new Response(content, httpStatus);
    }

    private Response handlePutRequest(Long id, String body) throws IOException {
        String title = toRequestContent(body).getTitle();

        Task task = tasks.update(id, title);

        if(task != null) {
            String content = taskToJson(task);
            HttpStatus httpStatus = HttpStatus.Ok;

            return new Response(content, httpStatus);
        } else {
            return new Response(HttpStatus.NotFound);
        }
    }

    private Response handleDeleteRequest(Long id) throws IOException {
        Task task = tasks.delete(id);

        if(task != null) {
            String content = taskToJson(task);
            HttpStatus httpStatus = HttpStatus.NoContent;

            return new Response(content, httpStatus);
        } else {
            return new Response(HttpStatus.NotFound);
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
