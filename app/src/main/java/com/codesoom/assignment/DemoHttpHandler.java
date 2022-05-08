package com.codesoom.assignment;

import com.codesoom.assignment.method.CreateTask;
import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.response.ResponseCreated;
import com.codesoom.assignment.response.ResponseNoContent;
import com.codesoom.assignment.response.ResponseOK;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Task> tasks = new ArrayList<>();
    private int id = 1;

    public DemoHttpHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/tasks")) {
            handleCollection(exchange);
        } else if (path.startsWith("/tasks/")) {
            handleItem(exchange);
        }
    }

    private void handleCollection(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            getAllTasks(exchange);
        } else if (method.equals("POST")) {
            new CreateTask(exchange).handleItem();
        }
    }

    private void handleItem(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            getOneTask(exchange);
        } else if (method.equals("PUT")) {
            updateTask(exchange);
        } else if (method.equals("DELETE")) {
            deleteTask(exchange);
        }
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        Long id = getIdFromPath(exchange);
        Task task = tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        tasks.remove(task);
        String content = tasksToJSON();
        new ResponseNoContent(exchange).send(content);
    }

    private void updateTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader((inputStream))).lines()
                .collect(Collectors.joining("\n"));
        if (!body.isEmpty()) {
            Long id = getIdFromPath(exchange);
            Task task = tasks.stream()
                    .filter(t -> t.getId().equals(id))
                    .findAny()
                    .orElseThrow(IllegalArgumentException::new);
            Task changeTask = contentToTask(body, false);
            task.setTitle(changeTask.getTitle());
        }
        String content = tasksToJSON();
        new ResponseCreated(exchange).send(content);
    }

    //
    // private void createTask(HttpExchange exchange) throws IOException {
    //     InputStream inputStream = exchange.getRequestBody();
    //     String body = new BufferedReader(new InputStreamReader((inputStream))).lines()
    //             .collect(Collectors.joining("\n"));
    //     if (!body.isEmpty()) {
    //         Task task = contentToTask(body, true);
    //         tasks.add(task);
    //     }
    //     String content = tasksToJSON();
    //     new ResponseCreated(exchange).send(content);
    // }

    private void getAllTasks(HttpExchange exchange) throws IOException {
        String content = tasksToJSON();
        new ResponseOK(exchange).send(content);
    }

    private void getOneTask(HttpExchange exchange) throws IOException {
        String content = taskToJSON(getIdFromPath(exchange));
        new ResponseOK(exchange).send(content);
    }

    private Long getIdFromPath(HttpExchange exchange) {
        String idString = exchange.getRequestURI().getPath().substring("/tasks/".length());
        canConvertId(idString);
        return Long.valueOf(idString);
    }

    private void canConvertId(String idString) {
        try {
            Long.valueOf(idString);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private Task contentToTask(String content, boolean isPostMethod) throws JsonProcessingException {
        Task task = objectMapper.readValue(content, Task.class);
        task.setId(new Long(id));
        if (isPostMethod) {
            id += 1;
        }
        return task;
    }

    private String taskToJSON(Long id) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        Task task = tasks.stream().filter(t -> t.getId().equals(id)).findAny().orElseThrow(IllegalAccessError::new);
        objectMapper.writeValue(outputStream, task);
        return outputStream.toString();
    }

    private String tasksToJSON() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, tasks);
        return outputStream.toString();
    }
}
