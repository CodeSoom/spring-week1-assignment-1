package com.codesoom.assignment.web;

import com.codesoom.assignment.application.TaskApplicationService;
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

    public WebHandler(TaskApplicationService taskApplicationService) {
        this.taskApplicationService = taskApplicationService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Controller controller = new Controller(taskApplicationService);
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        HttpResponse response = null;
        String requestBody = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody()))
                .lines()
                .collect(Collectors.joining(""));
        HttpRequest request = new HttpRequest(path, requestBody);

        if (path.contains("/tasks/")) {
            switch (method) {
                case "GET":
                    response = controller.getTasksWithId(request);
                    break;
                case "PUT":
                    response = controller.putTask(request);
                    break;
                case "DELETE":
                    response = controller.deleteTask(request);
                    break;
            }
        }
        if (path.equals("/tasks")) {
            if (method.equals("GET")) {
                response = controller.getTasks(request);
            } else if (method.equals("POST")) {
                response = controller.postTask(request);
            }
        }
        if (path.equals("/")) {
            response = new HttpResponse(200, "Welcome to Las's service!");
        }
        if (response == null) {
            response = new HttpResponse(404, "Not Found");
        }
        writeHttpResponse(exchange, response);
    }

    private void writeHttpResponse(HttpExchange exchange, HttpResponse response) throws IOException {
        exchange.sendResponseHeaders(response.statusCode, response.content.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
