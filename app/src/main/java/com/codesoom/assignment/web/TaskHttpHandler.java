package com.codesoom.assignment.web;

import com.codesoom.assignment.TaskManager;
import com.codesoom.assignment.errors.MethodNotAllowedException;
import com.codesoom.assignment.errors.TaskIdNotFoundException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Collectors;

public class TaskHttpHandler implements HttpHandler {

    private static final String NOT_FOUND_MESSAGE = "Not Found.";

    private final TaskManager taskManager = TaskManager.getInstance();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpRequest httpRequest = createHttpRequest(httpExchange);
        String body = getParsedResponseBody(httpExchange);

        long taskId = httpRequest.getTaskIdFromPath();
        if (httpRequest.isReadAll()) {
            new HttpResponseOK(httpExchange).send(taskManager.getAllTasks());
        }

        if (httpRequest.isReadOne()) {
            try {
                new HttpResponseOK(httpExchange).send(taskManager.findTaskWith(taskId));
            } catch (TaskIdNotFoundException error) {
                new HttpResponseNotFound(httpExchange).send(error.getMessage());
            }
        }

        if (httpRequest.isCreateOne()) {
            if (body.isEmpty()) {
                new HttpResponseNoContent(httpExchange).send();
            }

            String createdTask = taskManager.createTask(body);

            new HttpResponseCreated(httpExchange).send(createdTask);
        }

        if (httpRequest.isUpdateOne()) {
            try {
                String updatedTask = taskManager.updateTask(taskId, body);

                new HttpResponseOK(httpExchange).send(updatedTask);
            } catch (TaskIdNotFoundException error) {
                new HttpResponseNotFound(httpExchange).send(error.getMessage());
            }
        }

        if (httpRequest.isDeleteOne()) {
            try {
                taskManager.deleteTask(taskId);

                new HttpResponseNoContent(httpExchange).send();
            } catch (TaskIdNotFoundException error) {
                new HttpResponseNotFound(httpExchange).send(error.getMessage());
            }
        }

        new HttpResponseNotFound(httpExchange).send(NOT_FOUND_MESSAGE);
    }

    private String getParsedResponseBody(HttpExchange httpExchange) {
        InputStream httpRequestBody = httpExchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(httpRequestBody))
            .lines()
            .collect(Collectors.joining("\n"));
    }

    private HttpRequest createHttpRequest(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String method = httpExchange.getRequestMethod();

        try {
            new HttpRequest(path, method);
        } catch (MethodNotAllowedException error) {
            new HttpResponseBadRequest(httpExchange).send(error.getMessage());
        }

        return new HttpRequest(path, method);
    }
}
