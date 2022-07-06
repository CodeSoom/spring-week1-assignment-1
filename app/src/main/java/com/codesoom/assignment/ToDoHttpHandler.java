package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.network.HttpRouter;
import com.codesoom.assignment.network.HttpMethod;
import com.codesoom.assignment.network.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * HTTP exchanges를 통해 전달받은 Request를 분석해서 할일 목록을 관리하고 적절한 Response를 전달하는 객체
 */
public class ToDoHttpHandler implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ToDoRepository repository = new ToDoRepository();

    private final HttpRouter router;

    public ToDoHttpHandler() {
        router = new HttpRouter();
        router.get("/tasks", (request, response) -> {
            this.sendGetResponseRoot(response);
        });
        router.get("/tasks/\\d*", (request, response) -> {
            this.sendGetResponseWithId(request.getPath(), response);
        });
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String method = exchange.getRequestMethod();

        if (method == null) {
            exchange.sendResponseHeaders(400, 0);
            return;
        }

        final URI uri = exchange.getRequestURI();
        final String path = uri.getPath();
        if (path == null) {
            exchange.sendResponseHeaders(400, 0);
            return;
        }

        System.out.println(method + " " + path);

        final String body = getRequestBody(exchange).orElse("");
        if (!body.isBlank()) {
            System.out.println(body);
        }

        final HttpMethod methodType = HttpMethod.convert(method);
        if (methodType == null) {
            sendNotFoundResponse(exchange);
            return;
        }

        final HttpResponse responder = new HttpResponse(exchange);

        router.route(exchange);

        switch (methodType) {
            case POST -> sendPostResponse(exchange, body);
            case PUT, PATCH -> sendPutResponse(exchange, path, body);
            case DELETE -> sendDeleteResponse(exchange, path);
        }
    }

    private void sendGetResponseWithId(String path, HttpResponse responder) throws IOException {
        Long taskId = -1L;

        try {
            taskId = Long.parseLong(path.split("/")[2]);
        } catch (final NumberFormatException e) {
            responder.send(400, "Failed to parse task id");
            return;
        }

        Optional<Task> task = repository.getTaskById(taskId);
        if (task.isPresent()) {
            responder.send(200, repository.taskToString(task.get()));
        } else {
            responder.send(404, "Not found task by id");
        }
    }

    private void sendGetResponseRoot(HttpResponse responder) throws IOException {
        try {
            responder.send(200, repository.getTasksJSON());
        } catch (IOException e) {
            responder.send(500, "Failed to convert tasks to JSON");
        }
    }

    private void sendNotFoundResponse(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 404, null);
    }

    private void sendDeleteResponse(HttpExchange exchange, String path) throws IOException {
        Long taskId = -1L;

        try {
            taskId = Long.parseLong(path.split("/")[2]);
        } catch (final NumberFormatException e) {
            sendResponse(exchange, 400, "Failed to parse task id");
            return;
        }

        Optional<Task> task = repository.getTaskById(taskId);
        if (task.isPresent()) {
            Task exitedTask = task.get();
            repository.deleteTask(exitedTask);
            sendResponse(exchange, 204, null);
        } else {
            sendResponse(exchange, 404, "Not found task by id");
        }
    }

    private void sendPutResponse(HttpExchange exchange, String path, String body) throws IOException {
        if (!checkPathHasTaskId(path)) {
            sendResponse(exchange, 400, "/tasks/:id 형식으로 path를 입력해주세요");
            return;
        }

        Long taskId = -1L;

        try {
            taskId = Long.parseLong(path.split("/")[2]);
        } catch (final NumberFormatException e) {
            sendResponse(exchange, 400, "Failed to parse task id");
            return;
        }

        Optional<Task> task = repository.getTaskById(taskId);
        if (task.isPresent()) {
            Task exitedTask = task.get();
            repository.updateTask(exitedTask, body);
            sendResponse(exchange, 200, repository.taskToString(task.get()));
        } else {
            sendResponse(exchange, 404, "Not found task by id");
        }
    }

    private void sendPostResponse(HttpExchange exchange, String body) throws IOException {
        if (body.isBlank()) {
            sendResponse(exchange, 400, "Failed to convert request body to Task");
            return;
        }

        try {
            Task task = repository.createTask(body);
            repository.addTask(task);
            sendResponse(exchange, 201, repository.taskToString(task));
        } catch (JsonProcessingException e) {
            sendResponse(exchange, 400, "Failed to convert request body to Task");
        } catch (IOException e) {
            sendResponse(exchange, 500, "Failed to convert Task to string");
        }
    }

    private void sendResponse(HttpExchange exchange, int responseCode, String content) throws IOException {
        new HttpResponse(exchange).send(responseCode, content);
    }

    private boolean checkPathHasTaskId(String path) {
        return path.matches("/tasks/\\d*");
    }

    private Optional<String> getRequestBody(HttpExchange exchange) {
        final InputStream inputStream = exchange.getRequestBody();
        if (inputStream == null) {
            return Optional.empty();
        }

        final String body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
        return Optional.ofNullable(body);
    }

}
