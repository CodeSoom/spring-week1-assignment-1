package com.codesoom.assignment.task.handler;

import com.codesoom.assignment.task.domain.Task;
import com.codesoom.assignment.task.repository.TaskRepository;
import com.codesoom.assignment.task.service.TaskService;
import com.codesoom.assignment.task.validator.TaskValidator;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    public static final String PATH = "/tasks";

    private final TaskService taskService = new TaskService();
    private final TaskValidator taskValidator = new TaskValidator();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        URI uri = httpExchange.getRequestURI();
        String path = uri.getPath();

        if (path.equals("/tasks")) {
            handleCollection(httpExchange, method);
        }

        if (path.startsWith("/tasks/")) {
            String stId = path.substring("/tasks/".length());
            if (!taskValidator.validTaskId(stId)) {
                handleResponse(400, httpExchange, "올바른 형식이 아닙니다.");
            }

            Long id = Long.parseLong(stId);
            handleItem(httpExchange, method, id);
        }
    }

    private void handleItem(HttpExchange httpExchange, String method, Long id) throws IOException {
        Task task = taskService.findByTaskId(id);

        if (task == null) {
            handleResponse(404, httpExchange, "");
            return;
        }

        switch (method){
            case "GET":
                handleDetail(httpExchange, task);
                break;

            case "PUT":
            case "PATCH":
                handleUpdate(httpExchange, task);
                break;

            case "DELETE":
                handleDelete(httpExchange, task);
                break;
        }
    }

    private void handleCollection(HttpExchange httpExchange, String method) throws IOException {
        switch (method) {
            case "GET":
                handleList(httpExchange);
                break;

            case "POST":
                handleSave(httpExchange);
                break;

            default:
                handleResponse(404, httpExchange, "지원하지 않는 HTTP Method 입니다.");
                break;
        }
    }

    private void handleSave(HttpExchange httpExchange) throws IOException {
        String body = getBody(httpExchange);

        if (!taskValidator.vaildBody(body)) {
            handleResponse(400, httpExchange, "title은 필수 값입니다.");
        }

        Task task = toTask(body);

        if (!taskValidator.vaildTaskTitle(task.getTitle())) {
            handleResponse(400, httpExchange, "title은 필수 값입니다.");
        }

        Task newTask = taskService.saveTask(task);

        handleResponse(201, httpExchange, toJson(newTask));
    }

    private void handleList(HttpExchange httpExchange) throws IOException {
        handleResponse(200, httpExchange, toJson(taskService.findALL()));
    }

    private void handleDetail(HttpExchange httpExchange, Task task) throws IOException {
        handleResponse(200, httpExchange, toJson(task));
    }

    private void handleUpdate(HttpExchange httpExchange, Task task) throws IOException {
        String body = getBody(httpExchange);

        if (!taskValidator.vaildBody(body)) {
            handleResponse(400, httpExchange, "body 값은 필수 값입니다.");
        }

        Task source = toTask(body);

        if (!taskValidator.vaildTaskTitle(source.getTitle())) {
            handleResponse(400, httpExchange, "title은 필수 값입니다.");
        }

        taskService.updateTask(task, source);

        handleResponse(200, httpExchange, toJson(task));
    }

    private void handleDelete(HttpExchange httpExchange, Task task) throws IOException {
        taskService.removeTask(task);

        handleResponse(200, httpExchange, "");
    }

    private void handleResponse(int httpCode, HttpExchange httpExchange, String content) throws IOException {
        httpExchange.sendResponseHeaders(httpCode, content.getBytes().length);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String getBody(HttpExchange httpExchange) {
        InputStream inputStream = httpExchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String toJson(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);

        return outputStream.toString();
    }


}
