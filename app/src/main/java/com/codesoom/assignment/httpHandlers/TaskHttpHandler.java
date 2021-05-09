package com.codesoom.assignment.httpHandlers;

import com.codesoom.assignment.enums.HttpMethod;
import com.codesoom.assignment.enums.HttpStatus;
import com.codesoom.assignment.httpHandlers.datas.HttpResponseData;
import com.codesoom.assignment.httpHandlers.exceptions.TaskNotFoundException;
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
        HttpResponseData response = new HttpResponseData(HttpStatus.OK, "초기화 값");

        try {
            if (path.equals("/tasks")) {
                response = processTasks(exchange);
            } else if (path.startsWith("/tasks/")) {
                response = processTask(exchange);
            }
            sendResponse(exchange, response.getStatus(), response.getContent());
        } catch (TaskNotFoundException e) {
            sendResponse(exchange, HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            sendResponse(exchange, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            sendResponse(exchange, HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private HttpResponseData processTask(HttpExchange exchange) throws IOException {
        Task task = getTask(exchange);
        HttpStatus status = HttpStatus.OK;
        String content = "";
        Task updateRequestedContent;

        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());
        switch (method) {
            case GET:
                content = toJson(task);
                break;
            case PUT:
            case PATCH:
                updateRequestedContent = toTask(parseRequestBody(exchange));
                task.setTitle(updateRequestedContent.getTitle());
                content = toJson(task);
                break;
            case DELETE:
                tasks.remove(task);
                status = HttpStatus.NO_CONTENT;
                break;
            default:
                break;
        }
        return new HttpResponseData(status, content);
    }

    private Task getTask(HttpExchange exchange) throws TaskNotFoundException {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        long taskId = Long.parseLong(path.substring("/tasks/".length()));
        Task task = findTaskById(taskId);
        if (task == null) {
            throw new TaskNotFoundException();
        }
        return task;
    }

    private HttpResponseData processTasks(HttpExchange exchange) throws IOException {
        HttpStatus status = HttpStatus.OK;
        String content = "";

        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());
        switch (method) {
            case GET:
                content = tasksToJSON();
                break;
            case POST:
                Task task = addTask(parseRequestBody(exchange));
                status = HttpStatus.CREATED;
                content = toJson(task);
                break;
            default:
                break;
        }
        return new HttpResponseData(status, content);
    }

    private void sendResponse(HttpExchange exchange, HttpStatus status, String content) throws IOException {
        // "WARNING: sendResponseHeaders: rCode = 204: forcing contentLen = -1" 경고 제거 위해, 204 상태코드일 때의 분기 추가
        long contentLength = status == HttpStatus.NO_CONTENT ? -1 : content.getBytes().length;
        exchange.sendResponseHeaders(status.getCode(), contentLength);
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
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private Task addTask(String requestBody) throws JsonProcessingException {
        if (requestBody.isBlank()) {
            throw new IllegalArgumentException("requestBody가 비었습니다.");
        }

        Task task = generateTask(requestBody);
        this.tasks.add(task);
        return task;
    }

    private Task generateTask(String requestBody) throws JsonProcessingException {
        Task task = toTask(requestBody);
        task.setId(generateTaskId());
        return task;
    }

    private Long generateTaskId() {
        return this.taskId++;
    }

    private Task findTaskById(long id) {
        return this.tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst().orElse(null);
    }

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
