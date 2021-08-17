package com.codesoom.assignment;

import com.codesoom.assignment.models.HttpResponse;
import com.codesoom.assignment.models.HttpStatusCode;
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
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private HashMap<Long, Task> tasks = new HashMap<>();
    private long id = 0L;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ....
        // 2. Path - "/tasks", "tasks/1", ...
        // 3. Headers, Body(Content)

        String method = httpExchange.getRequestMethod();

        URI uri = httpExchange.getRequestURI();

        String path = uri.getPath();

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path + " " + body);
        String[] pathElements = null;

        if (path != null) {
            pathElements = path.split("/");
        }

        boolean hasIdVariable = false;
        Long pathId = null;
        if (pathElements != null && pathElements.length > 2) {
            pathId = Long.parseLong(pathElements[pathElements.length - 1]);
        }
        long tempId = this.id;

        if ("GET".equals(method)) {
            handleGetMethod(tasks, httpExchange, path, pathId);
        } else if ("POST".equals(method)) { //task 생성
            handlePostMethod(tasks, httpExchange, body, tempId);
        } else if ("PATCH".equals(method) || "PUT".equals(method)) { // 특정 task 수정
            handlePatchMethod(tasks, httpExchange, body, pathId);
        } else if ("DELETE".equals(method)) { //특정 task 삭제
            handleDeleteMethod(httpExchange, pathId);
        }

    }

    private void handleGetMethod(HashMap<Long, Task> tasks, HttpExchange httpExchange, String path, Long pathId) throws IOException {
        HttpResponse httpResponse = null;
        if (Objects.equals(path, "/tasks")) { //전체 조회
            httpResponse = getTasks(tasks);
        } else if (pathId != null) { //특정 task 조회
            httpResponse = getTask(tasks, pathId);
        }
        sendResponse(httpExchange, httpResponse);
    }

    private void handlePostMethod(HashMap<Long, Task> tasks, HttpExchange httpExchange, String body, long tempId) throws IOException {
        HttpResponse httpResponse = generateTask(tasks, body, tempId);
        if (httpResponse.getHttpStatusCode() == HttpStatusCode.Created.getStatusCode()) {
            this.id = tempId + 1;
        }
        sendResponse(httpExchange, httpResponse);
    }

    private void handlePatchMethod(HashMap<Long, Task> tasks, HttpExchange httpExchange, String body, Long pathId) throws IOException {
        HttpResponse httpResponse = null;
        if (pathId != null) {
            httpResponse = modifyTask(tasks, body, pathId);
        }
        sendResponse(httpExchange, httpResponse);
    }

    private void handleDeleteMethod(HttpExchange httpExchange, Long pathId) throws IOException {
        HttpResponse httpResponse = null;
        if (pathId != null) {
            httpResponse = deleteTask(pathId);
        }
        sendResponse(httpExchange, httpResponse);
    }

    private void sendResponse(HttpExchange httpExchange, HttpResponse httpResponse) throws IOException {
        if (httpResponse != null) {
            httpExchange.sendResponseHeaders(httpResponse.getHttpStatusCode(), httpResponse.getLength());

            OutputStream outputStream = httpExchange.getResponseBody();
            if (httpExchange.getResponseBody() != null && httpResponse.getContent() != null) {
                outputStream.write(httpResponse.getContent().getBytes());
            }
            outputStream.flush();
            outputStream.close();
        } else {
            httpExchange.sendResponseHeaders(HttpStatusCode.InternalServerError.getStatusCode(), 0);
        }
    }

    private HttpResponse modifyTask(HashMap<Long, Task> tasks, String body, Long id) throws IOException {
        String content = null;
        int httpStatusCode;
        int length = 0;
        if (body != null && !body.isEmpty() && tasks.get(id) != null) {
            Task task = toTask(body);
            task.setId(id);
            tasks.put(id, task);
            content = taskToJSON(tasks, id);
            httpStatusCode = HttpStatusCode.Success.getStatusCode();
            length = content.getBytes().length;
        } else {
            httpStatusCode = HttpStatusCode.NotFound.getStatusCode();
        }
        return new HttpResponse(httpStatusCode, content, length);
    }

    private HttpResponse deleteTask(Long id) {
        String content = null;
        int httpStatusCode;
        int length = 0;
        if (tasks.get(id) != null) {
            tasks.remove(id);
            httpStatusCode = HttpStatusCode.NoContent.getStatusCode();
            length = -1;
        } else {
            content = "존재하지 않음";
            httpStatusCode = HttpStatusCode.NotFound.getStatusCode();
        }
        return new HttpResponse(httpStatusCode, content, length);
    }

    private HttpResponse generateTask(HashMap<Long, Task> tasks, String body, Long id) throws IOException {
        String content = null;
        int httpStatusCode;
        int length = 0;
        if (body != null && !body.isEmpty()) {
            Task task = toTask(body);
            long tempId = id + 1;
            task.setId(tempId);
            tasks.put(tempId, task);
            content = taskToJSON(tasks, tempId);
            length = content.getBytes().length;
            httpStatusCode = HttpStatusCode.Created.getStatusCode();
        } else {
            httpStatusCode = HttpStatusCode.NoContent.getStatusCode();
        }
        return new HttpResponse(httpStatusCode, content, length);
    }

    private HttpResponse getTasks(HashMap<Long, Task> tasks) throws IOException {
        String content = tasksToJSON(tasks);
        System.out.println("/tasks");
        int length = content.getBytes().length;
        int httpStatusCode = HttpStatusCode.Success.getStatusCode();
        return new HttpResponse(httpStatusCode, content, length);
    }

    private HttpResponse getTask(HashMap<Long, Task> tasks, Long id) throws IOException {
        String content = null;
        int httpStatusCode;
        int length = 0;
        if (tasks.get(id) != null) {
            content = taskToJSON(tasks, id);
            httpStatusCode = HttpStatusCode.Success.getStatusCode();
            length = content.getBytes().length;
        } else {
            httpStatusCode = HttpStatusCode.NotFound.getStatusCode();
        }
        return new HttpResponse(httpStatusCode, content, length);
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJSON(HashMap<Long, Task> tasks) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, new ArrayList<>(tasks.values()));

        return outputStream.toString();
    }

    private String taskToJSON(HashMap<Long, Task> tasks, Long id) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks.get(id));

        return outputStream.toString();
    }
}
