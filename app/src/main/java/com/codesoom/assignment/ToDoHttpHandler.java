package com.codesoom.assignment;

import com.codesoom.assignment.models.Task;
import com.codesoom.assignment.network.HttpResponseCode;
import com.codesoom.assignment.network.HttpRouter;
import com.codesoom.assignment.network.HttpMethod;
import com.codesoom.assignment.network.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Optional;

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
        router.post("/tasks", (request, response) -> {
            this.sendPostResponse(response, request.getBody());
        });
        router.put("/tasks/\\d*", (request, response) -> {
            this.sendPutResponse(response, request.getPath(), request.getBody());
        });
        router.patch("/tasks/\\d*", (request, response) -> {
            this.sendPutResponse(response, request.getPath(), request.getBody());
        });
        router.delete("/tasks/\\d*", (request, response) -> {
            this.sendDeleteResponse(response, request.getPath());
        });
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        router.route(exchange);
    }

    private void sendGetResponseWithId(String path, HttpResponse response) throws IOException {
        Long taskId;

        try {
            taskId = Long.parseLong(path.split("/")[2]);
        } catch (final NumberFormatException e) {
            response.send(HttpResponseCode.BadRequest, "Failed to parse task id");
            return;
        }

        Optional<Task> task = repository.getTaskById(taskId);
        if (task.isPresent()) {
            response.send(HttpResponseCode.OK, repository.taskToString(task.get()));
        } else {
            response.send(HttpResponseCode.NotFound, "Not found task by id");
        }
    }

    private void sendGetResponseRoot(HttpResponse response) throws IOException {
        try {
            response.send(HttpResponseCode.OK, repository.getTasksJSON());
        } catch (IOException e) {
            response.send(HttpResponseCode.InternalServerError, "Failed to convert tasks to JSON");
        }
    }

    private void sendDeleteResponse(HttpResponse response, String path) throws IOException {
        Long taskId;

        try {
            taskId = Long.parseLong(path.split("/")[2]);
        } catch (final NumberFormatException e) {
            response.send(HttpResponseCode.BadRequest, "Failed to parse task id");
            return;
        }

        Optional<Task> task = repository.getTaskById(taskId);
        if (task.isPresent()) {
            Task exitedTask = task.get();
            repository.deleteTask(exitedTask);
            response.send(HttpResponseCode.NoContent, null);
        } else {
            response.send(HttpResponseCode.NotFound, "Not found task by id");
        }
    }

    private void sendPutResponse(HttpResponse response, String path, String body) throws IOException {
        Long taskId;

        try {
            taskId = Long.parseLong(path.split("/")[2]);
        } catch (final NumberFormatException e) {
            response.send(HttpResponseCode.BadRequest, "Failed to parse task id");
            return;
        }

        Optional<Task> task = repository.getTaskById(taskId);
        if (task.isPresent()) {
            Task exitedTask = task.get();
            repository.updateTask(exitedTask, body);
            response.send(HttpResponseCode.OK, repository.taskToString(task.get()));
        } else {
            response.send(HttpResponseCode.NotFound, "Not found task by id");
        }
    }

    private void sendPostResponse(HttpResponse response, String body) throws IOException {
        if (body == null || body.isBlank()) {
            response.send(HttpResponseCode.BadRequest, "Failed to convert request body to Task");
            return;
        }

        try {
            Task task = repository.createTask(body);
            repository.addTask(task);
            response.send(HttpResponseCode.Created, repository.taskToString(task));
        } catch (JsonProcessingException e) {
            response.send(HttpResponseCode.BadRequest, "Failed to convert request body to Task");
        } catch (IOException e) {
            response.send(HttpResponseCode.InternalServerError, "Failed to convert Task to string");
        }
    }

}
