package com.codesoom.assignment;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.checkerframework.checker.nullness.Opt;

import java.io.*;
import java.net.URI;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyHttpHandler implements HttpHandler {
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
            exchange.sendResponseHeaders(HttpStatus.OK.getCode(), content.getBytes().length);
        }

        if (method.equals("GET") && pattern.matcher(path).matches()) {
            Long pathVariable = extractPathVariable(path);

            Optional<Task> foundTask = tasks.findTask(pathVariable);
            if (foundTask.isPresent()) {
                content = JsonConverter.taskToJson(foundTask.get());
                exchange.sendResponseHeaders(HttpStatus.OK.getCode(), content.getBytes().length);
            }
            if (foundTask.isEmpty()) {
                System.out.println("not found task" + foundTask);
                exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
            }
        }

        if (method.equals("POST") && path.equals("/tasks")) {
            content = "create a new task";
            if (!body.isBlank()) {
                Task task = JsonConverter.toTask(body);
                task.setId(IdGenerator.generate());
                tasks.addTask(task);
                exchange.sendResponseHeaders(HttpStatus.CREATED.getCode(), content.getBytes().length);
            }
        }

        if (method.equals("PUT") && pattern.matcher(path).matches()) {
            content = "update a task";
            Long pathVariable = extractPathVariable(path);

            if (!body.isBlank()) {
                Optional<Task> foundTask = tasks.findTask(pathVariable);

                if (foundTask.isPresent()) {
                    foundTask.get().setTitle(JsonConverter.extractValue(body));
                    exchange.sendResponseHeaders(HttpStatus.OK.getCode(), content.getBytes().length);
                }
                if (foundTask.isEmpty()) {
                    exchange.sendResponseHeaders(HttpStatus.NOT_FOUND.getCode(), 0);
                }
            }
        }

        if (method.equals("DELETE") && pattern.matcher(path).matches()) {
            content = "delete a task";
            Long pathVariable = extractPathVariable(path);

            Optional<Task> foundTask = tasks.findTask(pathVariable);
            if (foundTask.isPresent()) {
                tasks.remove(foundTask.get());
                exchange.sendResponseHeaders(HttpStatus.NO_CONTENT.getCode(), content.getBytes().length);
            }
            if (foundTask.isEmpty()) {
                exchange.sendResponseHeaders(HttpStatus.OK.getCode(), 0);
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
