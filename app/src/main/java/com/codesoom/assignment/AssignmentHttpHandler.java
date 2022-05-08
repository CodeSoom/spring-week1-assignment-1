package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentHttpHandler implements HttpHandler {
    private TaskRepository taskRepository = TaskRepository.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();
        final String path = exchange.getRequestURI().getPath();

        final InputStream inputStream = exchange.getRequestBody();
        final String body = new BufferedReader(new InputStreamReader(inputStream))
                        .lines()
                        .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);

        String content = "Hello, world!";

        if ("GET".equals(method) && path.equals("/tasks")) {
            getTasks(exchange);
            return;
        }

        if ("GET".equals(method) && path.startsWith("/tasks/")) {
            getTask(exchange, path);
            return;
        }

        if ("POST".equals(method) && path.equals("/tasks")) {
            setTasks(exchange, body);
            return;
        }

        if ("PUT".equals(method) && path.startsWith("/tasks/")) {
            setTask(exchange, path, body);
            return;
        }

        if ("DELETE".equals(method) && path.startsWith("/tasks/")) {
            removeTask(exchange, path);
        }

    }

    private void removeTask(HttpExchange exchange, String path) throws IOException {
        Long id = Long.parseLong(path.split("/")[2]);
        taskRepository.delete(id);
        sendResponse(exchange, "", HttpStatus.OK);
        return;
    }

    private void setTask(HttpExchange exchange, String path, String body) throws IOException {
        Long id = Long.parseLong(path.split("/")[2]);
        Task newTask = toTask(body);
        Task changeTask = taskRepository.update(id, newTask);
        sendResponse(exchange, tasksToJson(changeTask), HttpStatus.OK);
        return;
    }

    private void setTasks(HttpExchange exchange, String body) throws IOException {
        Task task = toTask(body);
        Task savedTask = taskRepository.save(task);
        sendResponse(exchange, tasksToJson(savedTask), HttpStatus.Created);
        return;
    }

    private void getTask(HttpExchange exchange, String path) throws IOException {
        Long id = Long.parseLong(path.split("/")[2]);
        Task findTask = taskRepository.findById(id);
        sendResponse(exchange, tasksToJson(findTask), HttpStatus.OK);
        return;
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        List<Task> findTasks = taskRepository.findAll();
        sendResponse(exchange, tasksToJson(findTasks), HttpStatus.OK);
        return;
    }

    private void sendResponse(HttpExchange exchange, String content, HttpStatus code) throws IOException {
        exchange.sendResponseHeaders(code.getHttpStatus(), content.getBytes().length);

        try (OutputStream outputStream = exchange.getResponseBody()){
            outputStream.write(content.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Task toTask(String content) throws JsonProcessingException {
        return objectMapper.readValue(content, Task.class);
    }

    private String tasksToJson(Object obj) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(outputStream, obj);

        return outputStream.toString();
    }
}
