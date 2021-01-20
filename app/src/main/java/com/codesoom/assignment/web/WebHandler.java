package com.codesoom.assignment.web;

import com.codesoom.assignment.application.TaskApplicationService;
import com.codesoom.assignment.application.TaskJsonTransfer;
import com.codesoom.assignment.domain.NotFoundTask;
import com.codesoom.assignment.domain.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Optional;
import java.util.stream.Collectors;

public class WebHandler implements HttpHandler {
    TaskApplicationService taskApplicationService;
    TaskJsonTransfer transfer;

    public WebHandler(TaskApplicationService taskApplicationService) {
        this.taskApplicationService = taskApplicationService;
        this.transfer = new TaskJsonTransfer();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            if (path.equals("/tasks")) {
                String content = transfer.taskListToJson(taskApplicationService.getAllTasks());
                setJsonToResponseBody(exchange, content, 200);
            } else if (path.contains("/task/")) {
                String resourceId = path.split("/")[2];
                Long taskId = (long) Integer.parseInt(resourceId);

                Optional<Task> task = taskApplicationService.findTask(taskId);

                if (task.isEmpty()) {
                    String content = "Not Found";
                    setJsonToResponseBody(exchange, content, 404);
                    return;
                }
                String content = transfer.taskToJson(task.get());
                setJsonToResponseBody(exchange, content, 200);
            } else {
                exchange.sendResponseHeaders(200, 0);
            }
        } else if (method.equals("POST")) {
            if (path.equals("/task")) {
                String requestBody = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody()))
                        .lines()
                        .collect(Collectors.joining(""));
                Task requestTask = transfer.jsonStringToTask(requestBody);

                Long taskId = taskApplicationService.createTask(requestTask.getTitle());
                Optional<Task> createdTask = taskApplicationService.findTask(taskId);

                if (createdTask.isEmpty()) {
                    String content = "Not Found";
                    setJsonToResponseBody(exchange, content, 404);
                    return;
                }
                String content = transfer.taskToJson(createdTask.get());
                setJsonToResponseBody(exchange, content, 201);
            }
        }
    }

    private void setJsonToResponseBody(HttpExchange exchange, String content, int statusCode) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, content.getBytes().length);

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
