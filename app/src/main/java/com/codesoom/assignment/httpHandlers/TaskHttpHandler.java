package com.codesoom.assignment.httpHandlers;

import com.codesoom.assignment.httpHandlers.exceptions.TaskNotFoundException;
import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    final private List<Task> tasks = new ArrayList<>();
    private long taskId = 0;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();

        String content = "";
        int statusCode = 200;

        try {
            if (path.equals("/tasks")) {
                doRequestedActionOfTasksAndSendResponse(exchange);
            } else if (path.startsWith("/tasks/")) {
                doRequestedActionOfTaskAndSendResponse(exchange);
            }
        } catch (TaskNotFoundException e) {
            System.out.println("Exception: " + e.getMessage());
            statusCode = 404;
            content = e.getMessage();
            sendResponse(exchange, statusCode, content);
        } catch (IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
            statusCode = 400;
            content = e.getMessage();
            sendResponse(exchange, statusCode, content);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            statusCode = 409;
            content = e.getMessage();
            sendResponse(exchange, statusCode, content);
        }
    }

    private void doRequestedActionOfTaskAndSendResponse(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        long taskId = Long.parseLong(path.substring(7));
        Task task = findTaskById(taskId);
        if (task == null) {
            throw new TaskNotFoundException();
        }

        int statusCode = 200;
        String content = "";
        Task updateRequestedContent;

        String requestMethod = exchange.getRequestMethod();
        switch (requestMethod) {
            case "GET":
                content = toJson(task);
                break;
            case "PUT":
            case "PATCH":
                updateRequestedContent = toTask(parseRequestBody(exchange));
                task.setTitle(updateRequestedContent.getTitle());
                content = toJson(task);
                break;
            case "DELETE":
                tasks.remove(task);
                statusCode = 204;
                break;
            default:
                break;
        }
        sendResponse(exchange, statusCode, content);
    }

    private void doRequestedActionOfTasksAndSendResponse(HttpExchange exchange) throws IOException {
        int statusCode = 200;
        String content = "";

        String requestMethod = exchange.getRequestMethod();
        switch (requestMethod) {
            case "GET":
                content = tasksToJSON();
                break;
            case "POST":
                Task task = addTask(parseRequestBody(exchange));
                statusCode = 201;
                content = toJson(task);
                break;
            default:
                break;
        }
        sendResponse(exchange, statusCode, content);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String content) throws IOException {
        // "WARNING: sendResponseHeaders: rCode = 204: forcing contentLen = -1" 경고 제거 위해, 204 상태코드일 때의 분기 추가
        long contentLength = statusCode == 204 ? -1 : content.getBytes().length;
        exchange.sendResponseHeaders(statusCode, contentLength);
        sendResponseBody(exchange, content);
    }

    private void sendResponseBody(HttpExchange exchange, String content) throws IOException {
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private String parseRequestBody(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        return requestBody;
    }

    private Task addTask(String requestBody) throws JsonProcessingException {
        if (requestBody.isBlank()) {
            throw new IllegalArgumentException("requestBody가 비었습니다.");
        }

        Task task = toTask(requestBody);
        task.setId(taskId);
        taskId++;
        this.tasks.add(task);

        return task;
    }

    private Task findTaskById(long id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }


    // TODO: objectMapper 메서드들 확인할 것.
    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String toJson(Object object) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, object);

        return outputStream.toString();
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, this.tasks);

        return outputStream.toString();
    }
}
