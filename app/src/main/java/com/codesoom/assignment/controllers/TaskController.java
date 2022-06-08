package com.codesoom.assignment.controllers;

import com.codesoom.assignment.services.TaskService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.stream.Collectors;

public class TaskController implements HttpHandler {

    private final TaskService taskService = new TaskService();
    private final GetController getController = new GetController(taskService);
    private final PostController postController = new PostController(taskService);
    private final PutContoller putController = new PutContoller(taskService);
    private final DeleteController deleteController = new DeleteController(taskService);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = getReqMethod(exchange);
        String path = getReqPath(exchange);
        String body = getReqBody(exchange);

        switch (method) {
            case "GET":
                this.getController.route(exchange, path);
                break;
            case "POST":
                this.postController.route(exchange, path, body);
                break;
            case "PUT":
            case "PATCH":
                this.putController.route(exchange, path, body);
                break;
            case "DELETE":
                this.deleteController.route(exchange, path);
                break;
            default:
                throw new IOException();
        }
    }


    private String getReqMethod(@NotNull HttpExchange exchange) {
        return exchange.getRequestMethod();
    }

    private String getReqPath(@NotNull HttpExchange exchange) {
        return exchange.getRequestURI().getPath();
    }

    private String getReqBody(@NotNull HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }


    public static void sendResponse(@NotNull HttpExchange exchange, Integer statusCode, @NotNull String content) {
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

}
