package com.codesoom.assignment;

import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    TaskService taskService = new TaskService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        OutputStream outputStream = exchange.getResponseBody();

        String content = "";
        int returnCode = 404;
        if (method.equals("GET") && path.equals("/tasks")) {
            content = taskService.getTasks();
            returnCode = 200;
        } else if (method.equals("GET") && path.startsWith("/tasks")) {
            Long id = getId(path);
            content = taskService.getTask(id);
            if (content.equals("")) {
                returnCode = 404;
            } else {
                returnCode = 200;
            }
        } else if (method.equals("POST")) {
            content = taskService.addTask(body);
            returnCode = 201;
        } else if (method.equals("PUT")) {
            Long id = getId(path);
            content = taskService.updateTask(id, body);
            if (content.equals("")) {
                returnCode = 404;
            } else {
                returnCode = 200;
            }
        } else if (method.equals("DELETE")) {
            Long id = getId(path);
            boolean isDeleted = taskService.deleteTask(id);
            if (!isDeleted) {
                returnCode = 404;
            } else {
                returnCode = 204;
            }
        }
        exchange.sendResponseHeaders(returnCode, content.getBytes().length);
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Long getId(String path) {
        String[] identities = path.split("\\/");
        return Long.valueOf(identities[2]);
    }

}
