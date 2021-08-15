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
        HttpResponse httpResponse = null;
        if (path != null) {
            pathElements = path.split("/");
        }

        boolean hasIdVariable = false;
        if (pathElements != null && pathElements.length > 2) {
            hasIdVariable = true;
        }
        long id = this.id;

        if ("GET".equals(method) && Objects.equals(path, "/tasks")) { //전체 조회
            httpResponse = getTasks();
        } else if ("POST".equals(method) && Objects.equals(path, "/tasks")) { //task 생성
            httpResponse = generateTask(body, id);
            if (httpResponse.getHttpStatusCode() == HttpStatusCode.Created.getStatusCode()) {
                id = id + 1;
                this.id = id;
            }
        } else if ("GET".equals(method) && (hasIdVariable)) { //특정 task 조회
            id = Long.parseLong(pathElements[pathElements.length - 1]);

            httpResponse = getTask(id);
        } else if (("PATCH".equals(method) || "PUT".equals(method)) && (hasIdVariable)) { // 특정 task 수정
            id = Long.parseLong(pathElements[pathElements.length - 1]);
            httpResponse = modifyTask(body, id);
        } else if ("DELETE".equals(method) && (hasIdVariable)) { //특정 task 삭제
            id = Long.parseLong(pathElements[pathElements.length - 1]);
            httpResponse = deleteTask(id);
        }
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

    private HttpResponse modifyTask(String body, Long id) throws IOException {
        String content = null;
        int httpStatusCode;
        int length = 0;
        if (body != null && !body.isEmpty() && tasks.get(id) != null) {
            Task task = toTask(body);
            task.setId(id);
            tasks.put(id, task);
            content = taskToJSON(id);
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

    private HttpResponse generateTask(String body, Long id) throws IOException {
        String content = null;
        int httpStatusCode;
        int length = 0;
        if (body != null && !body.isEmpty()) {
            Task task = toTask(body);
            long tempId = id + 1;
            task.setId(tempId);
            tasks.put(tempId, task);
            content = taskToJSON(tempId);
            length = content.getBytes().length;
            httpStatusCode = HttpStatusCode.Created.getStatusCode();
        } else {
            httpStatusCode = HttpStatusCode.NoContent.getStatusCode();
        }
        return new HttpResponse(httpStatusCode, content, length);
    }

    private HttpResponse getTasks() throws IOException {
        String content = tasksToJSON();
        System.out.println("/tasks");
        int length = content.getBytes().length;
        int httpStatusCode = HttpStatusCode.Success.getStatusCode();
        return new HttpResponse(httpStatusCode, content, length);
    }

    private HttpResponse getTask(Long id) throws IOException {
        String content = null;
        int httpStatusCode;
        int length = 0;
        if (tasks.get(id) != null) {
            content = taskToJSON(id);
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

    private String tasksToJSON() throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, new ArrayList<>(tasks.values()));

        return outputStream.toString();
    }

    private String taskToJSON(Long id) throws IOException {

        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks.get(id));

        return outputStream.toString();
    }
}
