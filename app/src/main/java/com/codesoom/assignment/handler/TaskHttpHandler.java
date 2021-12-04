package com.codesoom.assignment.handler;

import com.codesoom.assignment.domain.Task;
import com.codesoom.assignment.validator.TaskValidator;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    public static final String PATH = "/tasks";

    private TaskValidator taskValidator = new TaskValidator();
    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Task> tasks = new ArrayList<>();


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] paths = path.split("\\/");
        int pathsLength = paths.length;
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        String content = "";
        Task task = null;

        switch (method) {
            case "GET":
                if (compareLengthTo(pathsLength, 3) && taskValidator.validTaskId(paths[2])) {
                    content = handleGetRequest(Integer.parseInt(paths[2]));
                } else if (compareLengthTo(pathsLength, 2)) {
                    content = handleGetRequest();
                } else {
                    handleResponse(400, exchange, "올바른 형식이 아닙니다.");
                }
                break;
            case "POST":
                if (!taskValidator.vaildBody(body)) {
                    handleResponse(400, exchange, "title은 필수 값입니다.");
                }

                task = toTask(body);

                if (!taskValidator.vaildTaskTitle(task.getTitle())) {
                    handleResponse(400, exchange, "title은 필수 값입니다.");
                }

                content = handlePostRequest(task);
                break;

            case "PUT":
            case "PATCH":
                if (!compareLengthTo(pathsLength, 3) || !taskValidator.validTaskId(paths[2])) {
                    handleResponse(400, exchange, "올바른 형식이 아닙니다.");
                }
                if (!taskValidator.vaildBody(body)) {
                    handleResponse(400, exchange, "title은 필수 값입니다.");
                }

                task = toTask(body);

                if (!taskValidator.vaildTaskTitle(task.getTitle())) {
                    handleResponse(400, exchange, "title은 필수 값입니다.");
                }


                content = handlePutRequest(Integer.parseInt(paths[2]), task);
                break;
            case "DELETE":
                if (!compareLengthTo(pathsLength, 3)  || !taskValidator.validTaskId(paths[2])) {
                    handleResponse(400, exchange, "올바른 형식이 아닙니다.");
                }

                handleDeleteRequest(Integer.parseInt(paths[2]));
                break;
            default:
                handleResponse(400, exchange, "지원하지 않는 HTTP Method 입니다.");
                break;
        }

        handleResponse(200, exchange, content);
    }

    private void handleResponse(int httpCode, HttpExchange httpExchange, String content) throws IOException {
        httpExchange.sendResponseHeaders(httpCode, content.getBytes().length);

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String handleGetRequest() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private String handleGetRequest(int id) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream,
                tasks.stream().filter(it -> it.getId() == id).findFirst().get());

        return outputStream.toString();
    }

    private String handlePostRequest(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        long index = tasks.size();
        task.setId(index + 1);
        tasks.add(task);

        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String handlePutRequest(int id, Task newTask) throws JsonProcessingException {
        Task task = tasks.stream().filter(it -> it.getId() == id).findFirst().get();
        tasks.remove(task);

        long index = tasks.size();
        newTask.setId(task.getId());
        tasks.add(newTask);

        tasks.sort(Comparator.comparing(Task::getId));
        return objectMapper.writeValueAsString(newTask);
    }

    private void handleDeleteRequest(int id) {
        Task task = tasks.stream().filter(it -> it.getId() == id).findFirst().get();
        tasks.remove(task);
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private Boolean compareLengthTo(int length, int targetLength) {
        return length == targetLength;
    }
}
