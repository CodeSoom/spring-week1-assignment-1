package com.codesoom.assignment;

import com.codesoom.assignment.service.TaskService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.stream.Collectors;

public class DemoHttpHandler implements HttpHandler {

    private TaskService taskService = new TaskService();

    private static final int HTTP_STATUS_CODE_OK = 200;
    private static final int HTTP_STATUS_CODE_CREATED = 201;
    private static final int HTTP_STATUS_CODE_NO_CONTENT = 204;
    private static final int HTTP_STATUS_CODE_NOT_FOUND= 404;

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
        int httpStatusCode = HTTP_STATUS_CODE_NOT_FOUND;
        if (method.equals("GET") && path.equals("/tasks")) {
            content = taskService.getTasks();
            httpStatusCode = HTTP_STATUS_CODE_OK;
        } else if (method.equals("GET") && path.startsWith("/tasks")) {
            Long id = getId(path);
            content = taskService.getTask(id);
            if (!content.equals("")) {
                httpStatusCode = HTTP_STATUS_CODE_OK;
            }
        } else if (method.equals("POST")) {
            content = taskService.addTask(body);
            httpStatusCode = HTTP_STATUS_CODE_CREATED;
        } else if (method.equals("PUT")) {
            Long id = getId(path);
            content = taskService.updateTask(id, body);
            if (!content.equals("")) {
                httpStatusCode = HTTP_STATUS_CODE_OK;
            }
        } else if (method.equals("DELETE")) {
            Long id = getId(path);
            boolean isDeleted = taskService.deleteTask(id);
            if (isDeleted) {
                httpStatusCode = HTTP_STATUS_CODE_NO_CONTENT;
            }
        }
        exchange.sendResponseHeaders(httpStatusCode, content.getBytes().length);
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Long getId(String path) {
        String[] identities = path.split("\\/");
        return Long.valueOf(identities[2]);
    }

}
