package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.util.IdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    private List<Task> tasks;
    private IdGenerator idGenerator;
    private ObjectMapper mapper;
    private int statusCode = 404;

    public DemoHttpHandler() {
        tasks = new ArrayList<>();
        idGenerator = new IdGenerator();
        mapper = new ObjectMapper();

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // REST - CRUD
        // 1. Method - GET, POST, PUT/PATCH, DELETE, ...
        // 2. Path - "/", "/tasks", "/tasks/1", ...
        // 3. Headers, Body(Content)

        String requestMethod = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(requestMethod + " " + path);

        StringTokenizer st = new StringTokenizer(path, "/");
        int tokenSize = st.countTokens();

        String id = "";
        while(st.hasMoreTokens()) {
            id = st.nextToken();
        }

        String content = "";

        if (tokenSize <= 1) {
            if (requestMethod.equals("GET") && (path.equals("/tasks"))) {
                content = tasksToJson(tasks);
                this.statusCode = 200;
            }

            if (requestMethod.equals("POST") && path.equals("/tasks")) {
                if (!requestBody.isBlank()) {
                    Task task = new Task();
                    task = toTask(requestBody);
                    task.setId(idGenerator.generate());
                    tasks.add(task);
                }
                content = tasksToJson(tasks);
                this.statusCode = 201;
            }

        } else {

            int taskId = Integer.parseInt(id);

            if (requestMethod.equals("GET") && path.equals("/tasks/" + taskId)) {
                if (getTask(taskId) == null) {
                    this.statusCode = 404;
                } else {
                    Task task = getTask(taskId);
                    if (task == null) {return;}
                    content = taskToJson(task);
                    this.statusCode = 200;
                }
            }

            if (requestMethod.equals("PUT") && path.equals("/tasks/" + taskId)) {
                if (getTask(taskId) == null) {
                    this.statusCode = 404;
                } else {
                    if (!requestBody.isBlank()) {
                        Task task = getTask(taskId);
                        task.setTitle(mapper.readValue(requestBody, Task.class).getTitle());
                        content = taskToJson(task);
                    }
                    this.statusCode = 200;
                }
            }

            if (requestMethod.equals("DELETE") && path.equals("/tasks/" + taskId)) {
                if (getTask(taskId) == null) {
                    this.statusCode = 404;
                } else {
                    Task task = getTask(taskId);
                    tasks.remove(task);
                    this.statusCode = 204;
                }
            }

        }
        exchange.sendResponseHeaders(this.statusCode, content.getBytes().length);
        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(content.getBytes());
        responseBody.flush();
        responseBody.close();
    }

    private String taskToJson(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String tasksToJson(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private Task toTask(String content) throws JsonProcessingException {
        return mapper.readValue(content, Task.class);
    }

    private Task getTask(int taskId) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }
}
