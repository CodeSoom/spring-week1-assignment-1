package com.codesoom.assignment;

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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    private Long lastTaskId = 1L;

    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(httpExchange);
        System.out.println(httpRequest);

        InputStream httpRequestBody = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(httpRequestBody))
            .lines()
            .collect(Collectors.joining("\n"));

        String content;
        int httpStatusCode;

        if (httpRequest.isMatchMethod("GET") && httpRequest.isMatchPath("/tasks")) {
            content = tasksToJson();
            httpStatusCode = 200;
            new HttpResponse(httpExchange).send(httpStatusCode, content);
        }

        if (httpRequest.isMatchMethod("GET") && httpRequest.hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            Task task = getTaskFromId(taskId);
            if (task == null) {
                content = "Can't find task from your id.";
                httpStatusCode = 404;
                new HttpResponse(httpExchange).send(httpStatusCode, content);
            }

            content = taskToJson(task);
            httpStatusCode = 200;
            new HttpResponse(httpExchange).send(httpStatusCode, content);
        }

        if (httpRequest.isMatchMethod("POST") && httpRequest.isMatchPath("/tasks")) {
            if (!body.isEmpty()) {
                Task task = toTask(body);
                tasks.add(task);

                content = taskToJson(task);
                httpStatusCode = 201;
                new HttpResponse(httpExchange).send(httpStatusCode, content);
            }
        }

        if (httpRequest.isUpdateMethod() && httpRequest.pathStartsWith("/tasks") && httpRequest
            .hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            Task task = getTaskFromId(taskId);
            if (task == null) {
                content = "Can't find task from your id.";
                httpStatusCode = 404;
                new HttpResponse(httpExchange).send(httpStatusCode, content);
            }
            Task bodyTask = getTaskFromContent(body);

            task.setTitle(bodyTask.getTitle());

            content = taskToJson(task);
            httpStatusCode = 200;

            new HttpResponse(httpExchange).send(httpStatusCode, content);
        }

        if (httpRequest.isMatchMethod("DELETE") && httpRequest.pathStartsWith("/tasks")
            && httpRequest.hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            Task task = getTaskFromId(taskId);
            if (task == null) {
                content = "Can't find task from your id.";
                httpStatusCode = 404;
                new HttpResponse(httpExchange).send(httpStatusCode, content);
            }
            tasks.remove(task);

            content = taskToJson(task);
            httpStatusCode = 204;
            new HttpResponse(httpExchange).send(httpStatusCode, content);
        }

        new HttpResponse(httpExchange).send(400, "Bad Request");
    }

    private String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private Task getTaskFromContent(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private Task toTask(String content) throws JsonProcessingException {
        Task task = getTaskFromContent(content);
        task.setId(++lastTaskId);

        return task;
    }

    private Task getTaskFromId(long taskId) {
        return tasks.stream()
            .filter(task -> task.isMatchId(taskId))
            .findFirst()
            .orElse(null);
    }

    private String tasksToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
