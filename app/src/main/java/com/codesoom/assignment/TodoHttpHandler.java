package com.codesoom.assignment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {

    private Long newTaskId = 1L;

    private List<Task> taskList = new ArrayList<>();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();

        String requestBody = getRequestBody(httpExchange);

        handleTask(httpExchange, requestMethod, path, requestBody);
    }

    private String getRequestBody(HttpExchange httpExchange) {
        InputStream inputStream = httpExchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private void handleTask(HttpExchange httpExchange, String requestMethod, String path, String requestBody) throws IOException {
        String content = "";

        if (path.equals("/tasks")) {
            // Get TaskList
            if (requestMethod.equals(HttpMethod.GET.getName())) {
                content = toJson(taskList);
                sendResponse(httpExchange, HttpStatus.OK.getCode(), content);
            }

            // Create Task
            if (requestMethod.equals(HttpMethod.POST.getName())) {
                Task task = JsonToTask(requestBody);
                task.setId(newTaskId);
                newTaskId = newTaskId + 1;
                taskList.add(task);
                content = toJson(task);
                sendResponse(httpExchange, HttpStatus.CREATED.getCode(), content);
            }
        }

        if (path.startsWith("/tasks/")) {
            Long taskId = Long.parseLong(path.substring("/tasks/".length()));
            Optional<Task> searchTask = taskList.stream()
                                                .filter(task -> task.getId().equals(taskId))
                                                .findAny();
            // Get Task
            if (requestMethod.equals(HttpMethod.GET.getName())) {
                if (!searchTask.isPresent()) {
                    sendResponse(httpExchange, HttpStatus.NOT_FOUND.getCode(), "");
                    return;
                }
                content = toJson(searchTask.get());
                sendResponse(httpExchange, HttpStatus.OK.getCode(), content);
            }

            // Update Task
            if ((requestMethod.equals(HttpMethod.PUT.getName()) || requestMethod.equals(HttpMethod.PATCH.getName()))) {
                Task requestTask = JsonToTask(requestBody);
                if (!searchTask.isPresent()) {
                    sendResponse(httpExchange, HttpStatus.NOT_FOUND.getCode(), "");
                    return;
                }
                searchTask.get().setTitle(requestTask.getTitle());
                content = toJson(searchTask.get());
                sendResponse(httpExchange, HttpStatus.OK.getCode(), content);
            }

            // Delete Task
            if (requestMethod.equals(HttpMethod.DELETE.getName())) {
                if (!searchTask.isPresent()) {
                    sendResponse(httpExchange, HttpStatus.NOT_FOUND.getCode(), "");
                    return;
                }
                taskList.remove(searchTask.get());
                sendResponse(httpExchange, HttpStatus.NO_CONTENT.getCode(), "");
            }
        }
    }

    private void sendResponse(HttpExchange httpExchange, int statusCode, String content) throws IOException {
        httpExchange.sendResponseHeaders(statusCode, content.getBytes().length);
        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private Task JsonToTask(String requestBody) throws JsonProcessingException {
        return objectMapper.readValue(requestBody, Task.class);
    }

    private String toJson(Object taskObject) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, taskObject);

        return outputStream.toString();
    }
}
