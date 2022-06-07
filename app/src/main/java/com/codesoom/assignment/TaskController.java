package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class TaskController implements HttpHandler {

    private final TaskService taskService = new TaskService();
    
    public TaskController() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("First");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Second");

        this.taskService.register(task1);
        this.taskService.register(task2);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String reqMethod = exchange.getRequestMethod();
        switch (reqMethod) {
            case "GET":
                getMapper(exchange);
                break;
            case "POST":
                postMapper(exchange);
            case "PUT":
            case "PATCH":
                putMapper(exchange);
            case "DELETE":
                deleteMapper(exchange);
            default:
                throw new IOException(String.format("Method " + reqMethod +  " is not handled"));
        }
    }

    private void getMapper(@NotNull HttpExchange exchange) throws IOException {
        String reqPath = exchange.getRequestURI().getPath();

        if (isReqGettingOneTask(reqPath)) {
            Long id = Long.parseLong(reqPath.split("/")[2]); // Integer.parseInt() vs. Integer.valueOf() 어떤 것이 좋을까요?
            Task task = this.taskService.show(id);

            if (task != null) {
                sendResponse(exchange, 200, toJSON(task));
            }

            sendResponse(exchange, 404, "Task not found");

        } else if (isReqGettingAllTasks(reqPath)) {
            List<Task> tasks = this.taskService.showAll();

            if (tasks.size() > 0) {
                sendResponse(exchange, 200, toJSON(tasks));
            }

            sendResponse(exchange, 404, "There's no registered tasks");

        } else {
            sendResponse(exchange, 400, "This request can not be properly handled");
        }
    }

    private void postMapper(@NotNull HttpExchange exchange) {
        String reqPath = exchange.getRequestURI().getPath();
        if (isReqRegisterOneTask(reqPath)) {
            /**
             * TODO
             * 1. exchange에서 req body 받아와서 Task에 mapping
             * 2. taskService.register 메소드 호출
             * 3. 결과 response로 만들어서 반환
             */
        }
    }

    private void putMapper(@NotNull HttpExchange exchange) {
        String reqPath = exchange.getRequestURI().getPath();
        if (isReqModifyOneTask(reqPath)) {
            /**
             * TODO
             * 1. exchange에서 req body 받아와서 Task에 mapping,
             * 2. taskService.modify 메소드를 id와 함께 호출
             * 3. 결과 response로 만들어서 반환
             */
        }
    }

    private void deleteMapper(@NotNull HttpExchange exchange) {
        /**
         * TODO
         * 1. taskService.delete 메소드를 id와 함께 호출
         * 2. 결과 response로 만들어서 반환
         */
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

    private Boolean isReqRegisterOneTask(String path) {
        return path.matches("/tasks/?");
    }

    private Boolean isReqModifyOneTask(String path) {
        return path.matches("/tasks/\\d+/?");
    }

    private Boolean isReqDeleteOneTask(String path) {
        return path.matches("/tasks/\\d+/?");
    }
}
