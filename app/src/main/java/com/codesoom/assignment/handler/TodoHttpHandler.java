package com.codesoom.assignment.handler;

import com.codesoom.assignment.models.Response;
import com.codesoom.assignment.service.TodoService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.stream.Collectors;

public class TodoHttpHandler implements HttpHandler {
    private TodoService todoService = new TodoService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String content = "Hello World";
        Long taskId = 0L;
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        System.out.println(method + " " + path);
        Response response = new Response();

        if ("GET".equals(method) && path.equals("/tasks")) {
            todoService.getTasks(response);
            exchange.sendResponseHeaders(200, content.getBytes().length);
        } else if ("POST".equals(method) && path.equals("/tasks")) {
            todoService.postTask(response, body);
            exchange.sendResponseHeaders(201, content.getBytes().length);
        } else if ("PUT".equals(method) && path.matches("/tasks/[0-9\\w]+")) {
            taskId = Long.parseLong(path.split("/")[2]);
            todoService.putTask(response, taskId, body);
            exchange.sendResponseHeaders(200, content.getBytes().length);
        } else if ("GET".equals(method) && path.matches("/tasks/[0-9\\w]+")) {
            taskId = Long.parseLong(path.split("/")[2]);
            todoService.getTask(response, taskId);
            exchange.sendResponseHeaders(200, content.getBytes().length);
        } else if ("DELETE".equals(method) && path.matches("/tasks/[0-9\\w]+")) {
            taskId = Long.parseLong(path.split("/")[2]);
            todoService.deleteTask(response, taskId);
            exchange.sendResponseHeaders(204, content.getBytes().length);
        } else {
            content = "not found";
            exchange.sendResponseHeaders(404, content.getBytes().length);
        }

        response.sendResponseHeader(exchange);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
