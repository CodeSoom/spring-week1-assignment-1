package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TaskHttpHandler implements HttpHandler {

    private final TaskManager taskManager = new TaskManager();

    public TaskHttpHandler() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("First");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Second");

        this.taskManager.register(task1);
        this.taskManager.register(task2);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String reqMethod = exchange.getRequestMethod();
        switch (reqMethod) {
            case "GET":
                getMapper(exchange);
                break;
            case "POST":
            case "PUT":
            case "PATCH":
            case "DELETE":
            default:
        }
    }

    private void sendResponse(HttpExchange exchange, Integer statusCode, String content) {
        try {
            // Response Header
            exchange.sendResponseHeaders(statusCode, content.getBytes().length);

            // Response Body
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(content.getBytes());

            // Send and Close
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getMapper(HttpExchange exchange) throws IOException {
        String reqPath = exchange.getRequestURI().getPath();

        if (isReqGettingOneTask(reqPath)) {
            Long id = Long.parseLong(reqPath.split("/")[2]); // Integer.parseInt() vs. Integer.valueOf() 어떤 것이 좋을까요?
            Task task = this.taskManager.show(id);

            if (task != null) {
                sendResponse(exchange, 200, toJSON(task));
            }

            sendResponse(exchange, 404, "Task not found");

        } else if (isReqGettingAllTasks(reqPath)) {
            List<Task> tasks = this.taskManager.showAll();

            if (tasks.size() > 0) {
                sendResponse(exchange, 200, toJSON(tasks));
            }

            sendResponse(exchange, 404, "There's no registered tasks");

        } else {
            sendResponse(exchange, 400, "This request can not be properly handled");
        }
    }

    private String toJSON(Task task) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream, task);

        return outputStream.toString();
    }

    private String toJSON(List<Task> tasks) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(outputStream, tasks);

        return outputStream.toString();
    }

    private Boolean isReqGettingAllTasks(String path) {
        return path.matches("/tasks/?");
    }

    private Boolean isReqGettingOneTask(String path) {
        return path.matches("/tasks/\\d+/?");
    }

}
