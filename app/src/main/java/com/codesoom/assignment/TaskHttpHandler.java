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
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    private List<Task> tasks = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String method = httpExchange.getRequestMethod();

        System.out.println(method + " " + path);

        InputStream httpRequestBody = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(httpRequestBody))
            .lines()
            .collect(Collectors.joining("\n"));

        String content = "Hello, World!";
        int httpStatusCode = 200;

        if (method.equals("GET") && path.equals("/tasks")) {
            content = tasksToJson();
        }

        boolean isNumberMatchEndOfPath = Pattern.matches("/tasks/[0-9]+$", path);
        if (method.equals("GET") && isNumberMatchEndOfPath) {
            long taskId = getTaskIdFromPath(path);

            Task task = getTaskFromId(taskId);
            if (task == null) {
                content = "Can't find task from your id.";
                httpStatusCode = 404;
            } else {
                content = taskToJson(task);
            }
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            if (!body.isEmpty()) {
                Task task = toTask(body);
                tasks.add(task);

                content = taskToJson(task);
                httpStatusCode = 201;
            }
        }

        if ((method.equals("PUT") || method.equals("PATCH")) && isNumberMatchEndOfPath) {
            long taskId = getTaskIdFromPath(path);

            Task task = getTaskFromId(taskId);
            if (task == null) {
                content = "Can't find task from your id.";
                httpStatusCode = 404;
            } else {
                Task bodyTask = getTaskFromContent(body);

                task.setTitle(bodyTask.getTitle());

                content = taskToJson(task);
                httpStatusCode = 200;
            }
        }

        httpExchange.sendResponseHeaders(httpStatusCode, content.getBytes().length);

        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
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
        Long newTaskId = ((long) (tasks.size() + 1));

        Task task = getTaskFromContent(content);
        task.setId(newTaskId);

        return task;
    }

    private Task getTaskFromId(long taskId) {
        return tasks.stream()
            .filter(task -> task.isMatchId(taskId))
            .findFirst()
            .orElse(null);
    }

    private long getTaskIdFromPath(String path) {
        return Arrays.stream(path.split("/"))
            .skip(2)
            .mapToLong(Long::parseLong)
            .findFirst()
            .getAsLong();
    }

    private String tasksToJson() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }
}
