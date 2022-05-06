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
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                        .lines()
                        .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);

        String content = "Hello, world!";

        if ("GET".equals(method) && path.equals("/tasks")) {
            List<Task> findTasks = taskRepository.findAll();
            sendResponse(exchange, tasksToJson(findTasks), HttpStatus.OK);
            return;
        }

        if ("GET".equals(method) && path.startsWith("/tasks/")) {
            Long id = Long.parseLong(path.split("/")[2]);
            Task findTask = taskRepository.findById(id);
            sendResponse(exchange, tasksToJson(findTask), HttpStatus.OK);
            return;
        }

        if ("POST".equals(method) && path.equals("/tasks")) {
            Task task = toTask(body);
            Task savedTask = taskRepository.save(task);
            sendResponse(exchange, tasksToJson(savedTask), HttpStatus.Created);
            return;
        }

        if ("PUT".equals(method) && path.startsWith("/tasks/")) {
            Long id = Long.parseLong(path.split("/")[2]);
            Task newTask = toTask(body);
            Task changeTask = taskRepository.update(id, newTask);
            sendResponse(exchange, tasksToJson(changeTask), HttpStatus.OK);
            return;
        }

        if ("DELETE".equals(method) && path.startsWith("/tasks/")) {
            Long id = Long.parseLong(path.split("/")[2]);
            taskRepository.delete(id);
            sendResponse(exchange, "", HttpStatus.OK);
            return;
        }

    }

    private void sendResponse(HttpExchange exchange, String content, HttpStatus code) throws IOException {
        exchange.sendResponseHeaders(code.getHttpStatus(), content.getBytes().length);

        try (OutputStream outputStream = exchange.getResponseBody()){
            outputStream.write(content.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /*
                try-catch-finally 를 사용하라고 하셨는데
                finally 실행에서 log 같은 걸 남기라는 말인가요
             */
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
