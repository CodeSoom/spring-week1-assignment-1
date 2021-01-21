package com.codesoom.assignment.web;

import com.codesoom.assignment.application.TaskApplicationService;
import com.codesoom.assignment.application.TaskJsonTransfer;
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
    Controller controller;

    public WebHandler(TaskApplicationService taskApplicationService) {
        this.taskApplicationService = taskApplicationService;
        this.transfer = new TaskJsonTransfer();
        this.controller = new Controller(taskApplicationService);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        HttpResponse response = null;

        if (method.equals("GET")) {
            if (path.equals("/tasks")) {
                response = controller.getTasks();
            } else if (path.contains("/tasks/")) {
                Long id = parsePathToTaskId(path);
                response = controller.getTasksWithId(id);
            } else {
                response = new HttpResponse(200, "");
            }
        } else if (method.equals("POST")) {
            if (path.equals("/tasks")) {

                String requestBody = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody()))
                        .lines()
                        .collect(Collectors.joining(""));
                response = controller.postTask(requestBody);
            }
        } else if (method.equals("PUT")) {
            if (path.contains("/tasks")) {
                Long taskId = parsePathToTaskId(path);

                String requestBody = new BufferedReader(
                        new InputStreamReader(exchange.getRequestBody()))
                        .lines()
                        .collect(Collectors.joining(""));
                response = controller.putTask(taskId, requestBody);
            }
        } else if (method.equals("DELETE")) {
            if (path.contains("/tasks")) {
                Long taskId = parsePathToTaskId(path);
                response = controller.deleteTask(taskId);
            }
        } else {
            response = new HttpResponse(404, "");
        }
        writeHttpResponse(exchange, response);
    }

    private void writeHttpResponse(HttpExchange exchange, HttpResponse response) throws IOException {
        exchange.sendResponseHeaders(response.statusCode, response.content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.content.getBytes());
        outputStream.flush();
    }

    private Long parsePathToTaskId(String path){
        String resourceId = path.split("/")[2];
        return (long) Integer.parseInt(resourceId);
    }

}
