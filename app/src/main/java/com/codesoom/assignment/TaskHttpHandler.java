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

    public static final String NOT_FOUND_MESSAGE = "Not Found.";
    public static final String NOT_FOUND_TASK_ID_MESSAGE = "Can't find task from your id.";

    private final List<Task> tasks = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Long lastTaskId = 0L;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(httpExchange);
        System.out.println(httpRequest);

        InputStream httpRequestBody = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(httpRequestBody))
            .lines()
            .collect(Collectors.joining("\n"));

        if (httpRequest.isMatchMethod("GET") && httpRequest.isMatchPath("/tasks")) {
            new HttpResponseOK(httpExchange).send(tasksToJson());
        }

        if (httpRequest.isMatchMethod("GET") && httpRequest.hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            if (!existTaskFromId(taskId)) {
                new HttpResponseNotFound(httpExchange).send(NOT_FOUND_TASK_ID_MESSAGE);
            }

            Task content = findTaskFromId(taskId);
            new HttpResponseOK(httpExchange).send(taskToJson(content));
        }

        if (httpRequest.isMatchMethod("POST") && httpRequest.isMatchPath("/tasks")) {
            if (!body.isEmpty()) {
                Task task = toTask(body);
                tasks.add(task);

                new HttpResponseCreated(httpExchange).send(taskToJson(task));
            }
        }

        if (httpRequest.isUpdateMethod() && httpRequest.pathStartsWith("/tasks") && httpRequest
            .hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            if (!existTaskFromId(taskId)) {
                new HttpResponseNotFound(httpExchange).send(NOT_FOUND_TASK_ID_MESSAGE);
            }

            Task task = findTaskFromId(taskId);

            Task bodyTask = getTaskFromContent(body);
            task.setTitle(bodyTask.getTitle());

            new HttpResponseOK(httpExchange).send(taskToJson(task));
        }

        if (httpRequest.isMatchMethod("DELETE") && httpRequest.pathStartsWith("/tasks")
            && httpRequest.hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            if (!existTaskFromId(taskId)) {
                new HttpResponseNotFound(httpExchange).send(NOT_FOUND_TASK_ID_MESSAGE);
            }

            Task task = findTaskFromId(taskId);
            tasks.remove(task);

            new HttpResponseNoContent(httpExchange).send(taskToJson(task));
        }

        new HttpResponseNotFound(httpExchange).send(NOT_FOUND_MESSAGE);
    }

    private Task findTaskFromId(long taskId) {
        return tasks.stream()
            .filter(task -> task.isMatchId(taskId))
            .findFirst()
            .get();
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

    private boolean existTaskFromId(long taskId) {
        return tasks.stream()
            .anyMatch(task -> task.isMatchId(taskId));
    }

    private String tasksToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
