package com.codesoom.assignment.web;

import com.codesoom.assignment.TaskManager;
import com.codesoom.assignment.TaskMapper;
import com.codesoom.assignment.errors.TaskIdNotFoundException;
import com.codesoom.assignment.models.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    public static final String NOT_FOUND_MESSAGE = "Not Found.";

    private final TaskManager taskManager = TaskManager.getInstance();
    private final TaskMapper taskMapper = new TaskMapper();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpRequest httpRequest = new HttpRequest(httpExchange);
        System.out.println(httpRequest);

        InputStream httpRequestBody = httpExchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(httpRequestBody))
            .lines()
            .collect(Collectors.joining("\n"));

        if (httpRequest.isMatchMethod("GET") && httpRequest.isMatchPath("/tasks")) {
            new HttpResponseOK(httpExchange).send(taskMapper.toJson());
        }

        if (httpRequest.isMatchMethod("GET") && httpRequest.hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            try {
                new HttpResponseOK(httpExchange).send(taskMapper.toJsonWith(taskId));
            } catch (TaskIdNotFoundException error) {
                new HttpResponseNotFound(httpExchange).send(error.getMessage());
            }
        }

        if (httpRequest.isMatchMethod("POST") && httpRequest.isMatchPath("/tasks")) {
            if (body.isEmpty()) {
                new HttpResponseNoContent(httpExchange).send();
            }

            Task createdTask = taskManager.createTask(body);

            new HttpResponseCreated(httpExchange).send(taskMapper.toJsonWith(createdTask));
        }

        if (httpRequest.isUpdateMethod() && httpRequest.pathStartsWith("/tasks") && httpRequest
            .hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            try {
                Task updatedTask = taskManager.updateTask(taskId, body);

                new HttpResponseOK(httpExchange).send(taskMapper.toJsonWith(updatedTask));
            } catch (TaskIdNotFoundException error) {
                new HttpResponseNotFound(httpExchange).send(error.getMessage());
            }
        }

        if (httpRequest.isMatchMethod("DELETE") && httpRequest.pathStartsWith("/tasks")
            && httpRequest.hasTaskId()) {
            long taskId = httpRequest.getTaskIdFromPath();

            try {
                Task deletedTask = taskManager.deleteTask(taskId);

                new HttpResponseNoContent(httpExchange).send(taskMapper.toJsonWith(deletedTask));
            } catch (TaskIdNotFoundException error) {
                new HttpResponseNotFound(httpExchange).send(error.getMessage());
            }
        }

        new HttpResponseNotFound(httpExchange).send(NOT_FOUND_MESSAGE);
    }
}
