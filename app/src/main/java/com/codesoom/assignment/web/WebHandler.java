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
        if (exchange.getRequestURI().getPath().equals("/tasks")) {
            String content = transfer.taskListToJson(taskApplicationService.getAllTasks());
            setJsonToResponseBody(exchange, content, 200);
        } else if (exchange.getRequestURI().getPath().equals("/task")) {
            String requestBody = new BufferedReader(
                    new InputStreamReader(exchange.getRequestBody()))
                    .lines()
                    .collect(Collectors.joining(""));
            Task requestTask = transfer.jsonStringToTask(requestBody);

            try {
                Long taskId = taskApplicationService.createTask(requestTask.getTitle());
                Task createdTask = taskApplicationService.findTask(taskId);

                String content = transfer.taskToJson(createdTask);
                setJsonToResponseBody(exchange, content, 201);
            } catch (NotFoundTask notFoundTask) {
                notFoundTask.printStackTrace();
            }
        } else {
            exchange.sendResponseHeaders(200, 0);
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
