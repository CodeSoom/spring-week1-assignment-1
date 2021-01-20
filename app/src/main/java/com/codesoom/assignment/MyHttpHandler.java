package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {
    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int NOT_FOUND = 404;
    private static final int NO_CONTENT = 204;
    private Tasks tasks = new Tasks();

    private Pattern pattern = Pattern.compile("/tasks/([^/]+)");

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        InputStream inputStream = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        System.out.println(method + " " + path);
        String content = "Hello, world";

        if (method.equals("GET") && path.equals("/tasks")) {
            content = JsonConverter.tasksToJSON(tasks);
            exchange.sendResponseHeaders(OK, content.getBytes().length);
        }

        if (method.equals("GET") && pattern.matcher(path).matches()) {
            Long pathVariable = extractPathVariable(path);

            Optional<Task> foundTask = tasks.findTask(pathVariable);
            if (foundTask.isPresent()) {
                content = JsonConverter.taskToJson(foundTask.get());
                exchange.sendResponseHeaders(OK, content.getBytes().length);
            } else {
                System.out.println("not found task" + foundTask);
                exchange.sendResponseHeaders(NOT_FOUND, 0);
            }
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = "create a new task";
            if (!body.isBlank()) {
                Task task = JsonConverter.toTask(body);
                task.setId(IdGenerator.generate());
                tasks.addTask(task);
                exchange.sendResponseHeaders(CREATED, content.getBytes().length);
            }
        }

        if (method.equals("PUT") && pattern.matcher(path).matches()) {
            content = "update a task";
            Long pathVariable = extractPathVariable(path);

            if (!body.isBlank()) {
                Optional<Task> foundTask = tasks.findTask(pathVariable);
                if (foundTask.isPresent()) {
                    foundTask.get().setTitle(JsonConverter.extractValue(body));
                    exchange.sendResponseHeaders(OK, content.getBytes().length);
                } else {
                    exchange.sendResponseHeaders(NOT_FOUND, 0);
                }
            }
        }

        if (method.equals("DELETE") && pattern.matcher(path).matches()) {
            content = "delete a task";
            Long pathVariable = extractPathVariable(path);

            Optional<Task> foundTask = tasks.findTask(pathVariable);
            if (foundTask.isPresent()) {
                tasks.remove(foundTask.get());
                exchange.sendResponseHeaders(NO_CONTENT, content.getBytes().length);
            } else {
                exchange.sendResponseHeaders(NOT_FOUND, 0);
            }
        }

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(content.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private Long extractPathVariable(String path) {
        String[] paths = path.split("/");
        return Long.parseUnsignedLong(paths[2]);
    }
}
