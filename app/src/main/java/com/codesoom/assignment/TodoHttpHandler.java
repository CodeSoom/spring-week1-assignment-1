package com.codesoom.assignment;

import com.codesoom.assignment.models.*;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TasksStorage tasks = new TasksStorage();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        Response response = getResponse(new Request(path, method, body));

        exchange.sendResponseHeaders(response.getStatusCode(), response.getContentBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getContentBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Response getResponse(Request request) throws IOException {
        String method = request.getMethod();
        String body = request.getBody();

        if ("GET".equals(method) && !request.isPathWithId()) {
            return handleGetRequest();
        }

        if ("GET".equals(method) && request.isPathWithId()) {
            return handleGetRequest(request.getPathId());
        }

        if ("POST".equals(method)) {
            return handlePostRequest(body);
        }

        if ("PUT".equals(method)) {
            return handlePutRequest(request.getPathId(), body);
        }

        if ("DELETE".equals(method)) {
            return handleDeleteRequest(request.getPathId());
        }

        return new Response(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Response handleGetRequest() throws IOException {
        return new Response(tasksToJson(tasks.readAll()), HttpStatus.OK);
    }

    private Response handleGetRequest(Long id) throws IOException {
        Task task = tasks.read(id);

        if (task != null){
            return new Response(taskToJson(task), HttpStatus.OK);
        }

        return new Response(HttpStatus.NOT_FOUND);
    }

    private Response handlePostRequest(String body) throws IOException {
        String title = toRequestContent(body).getTitle();
        Task task = tasks.create(title);

        return new Response(taskToJson(task), HttpStatus.CREATED);
    }

    private Response handlePutRequest(Long id, String body) throws IOException {
        String title = toRequestContent(body).getTitle();
        Task task = tasks.update(id, title);

        if(task != null) {
            return new Response(taskToJson(task), HttpStatus.OK);
        }

        return new Response(HttpStatus.NOT_FOUND);
    }

    private Response handleDeleteRequest(Long id) throws IOException {
        Task task = tasks.delete(id);

        if(task != null) {
            return new Response(taskToJson(task), HttpStatus.NO_CONTENT);
        }

        return new Response(HttpStatus.NOT_FOUND);
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
